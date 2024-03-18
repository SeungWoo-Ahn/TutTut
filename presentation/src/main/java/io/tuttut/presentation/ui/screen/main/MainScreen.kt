package io.tuttut.presentation.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.tuttut.data.constant.CUSTOM_IMAGE
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.MainScreenTab
import io.tuttut.presentation.ui.component.MainTab
import io.tuttut.presentation.ui.component.TutTutFAB
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.getDDayStr

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    moveRecommend: () -> Unit,
    moveMy: () -> Unit,
    moveDetail: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.cachingGardenInfo()
    }
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val cropsInfoMap = viewModel.cropsInfoRepo.cropsInfoMap
    val cropsList = viewModel.cropsList.collectAsLazyPagingItems()
    val harvestedCropsList = viewModel.harvestedCropsList.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        viewModel.refreshCropsList(cropsList)
        viewModel.refreshHarvestedCropsList(harvestedCropsList)
    }
    MainScreen(
        modifier = modifier,
        topBarState = topBarState,
        cropsList = cropsList,
        harvestedCropsList = harvestedCropsList,
        selectedTab = selectedTab,
        cropsInfoMap = cropsInfoMap,
        onTab = viewModel::onTab,
        moveRecommend = moveRecommend,
        moveMy = moveMy,
        onItem = { viewModel.onItem(it, moveDetail) }
    )
}

@Composable
internal fun MainScreen(
    modifier: Modifier,
    topBarState: MainTopBarState,
    cropsList: LazyPagingItems<Crops>,
    harvestedCropsList: LazyPagingItems<Crops>,
    selectedTab: MainTab,
    cropsInfoMap: HashMap<String, CropsInfo>,
    onTab: (MainTab) -> Unit,
    moveRecommend: () -> Unit,
    moveMy: () -> Unit,
    onItem: (Crops) -> Unit,
) {
    val scrollState = rememberLazyListState()
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TutTutTopBar(
                title = if (topBarState is MainTopBarState.Success) topBarState.garden.name else "",
                needBack = false
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { moveMy() },
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "user-icon"
                )
            }
            MainScreenTab(
                selectedTab = selectedTab,
                onTab = onTab
            )
            if (
                cropsList.loadState.refresh is LoadState.Loading ||
                cropsList.loadState.append is LoadState.Loading ||
                harvestedCropsList.loadState.refresh is LoadState.Loading ||
                harvestedCropsList.loadState.append is LoadState.Loading
                ) {
                TutTutLoadingScreen()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenHorizontalPadding),
                    state = scrollState,
                ) {
                    if (selectedTab.isHarvested) {
                        items(
                            count = harvestedCropsList.itemCount,
                            key = harvestedCropsList.itemKey { it.id }
                        ) { index ->
                            val crops = harvestedCropsList[index]
                            if (crops != null) {
                                CropsItem(
                                    crops = crops,
                                    isHarvested = true,
                                    cropsInfoMap = cropsInfoMap,
                                    onClick = { onItem(crops) }
                                )
                            }
                        }
                    }
                    else {
                        items(
                            count = cropsList.itemCount,
                            key = cropsList.itemKey { it.id }
                        ) { index ->
                            val crops = cropsList[index]
                            if (crops != null) {
                                CropsItem(
                                    crops = crops,
                                    isHarvested = false,
                                    cropsInfoMap = cropsInfoMap,
                                    onClick = { onItem(crops) }
                                )
                            }
                        }
                    }
                }
            }
        }
        TutTutFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = screenHorizontalPadding),
            text = stringResource(id = R.string.add),
            expanded = !scrollState.isScrollInProgress,
            onClick = moveRecommend
        )
    }
}

@Composable
fun CropsItem(
    modifier: Modifier = Modifier,
    crops: Crops,
    isHarvested: Boolean,
    cropsInfoMap: HashMap<String, CropsInfo>,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TutTutImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                url = cropsInfoMap[crops.key]?.imageUrl ?: CUSTOM_IMAGE
            )
            Spacer(modifier = Modifier.width(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = crops.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = crops.nickName, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isHarvested) {
                        Text(
                            modifier = Modifier.weight(2f),
                            text = stringResource(id = R.string.harvested),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.ic_water),
                                contentDescription = "water-icon"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = crops.wateringInterval?.let { getDDayStr(crops.lastWatered, it) } ?: "-",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.ic_harvest),
                                contentDescription = "harvest-icon"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = crops.growingDay?.let { getDDayStr(crops.plantingDate, it) } ?: "-",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.ic_diary),
                            contentDescription = "diary-icon"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${crops.diaryCnt} 개",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
    }

}