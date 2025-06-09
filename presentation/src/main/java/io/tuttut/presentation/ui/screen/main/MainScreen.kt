package io.tuttut.presentation.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.network.constant.CUSTOM_IMAGE
import io.tuttut.data.network.model.CropsDto
import io.tuttut.data.network.model.CropsInfoDto
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.MainScreenTab
import io.tuttut.presentation.ui.component.MainTab
import io.tuttut.presentation.ui.component.NoResults
import io.tuttut.presentation.ui.component.TutTutFAB
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.clickableWithOutRipple
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
        viewModel.saveUserId()
        viewModel.cachingGardenInfo()
    }
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val cropsInfoMap = viewModel.cropsInfoRepo.cropsInfoMap
    MainScreen(
        modifier = modifier,
        topBarState = topBarState,
        uiState = uiState,
        selectedTab = selectedTab,
        cropsInfoMap = cropsInfoMap,
        onTab = viewModel::onTab,
        onItem = { viewModel.onItem(it, moveDetail) },
        moveRecommend = moveRecommend,
        moveMy = moveMy,
    )
}

@Composable
internal fun MainScreen(
    modifier: Modifier,
    topBarState: MainTopBarState,
    uiState: MainUiState,
    selectedTab: MainTab,
    cropsInfoMap: HashMap<String, CropsInfoDto>,
    onTab: (MainTab) -> Unit,
    onItem: (CropsDto) -> Unit,
    moveRecommend: () -> Unit,
    moveMy: () -> Unit,
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
                        .clickableWithOutRipple(
                            onClick = moveMy,
                            interactionSource = remember(::MutableInteractionSource)
                        ),
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "user-icon"
                )
            }
            MainScreenTab(
                selectedTab = selectedTab,
                onTab = onTab
            )
            when (uiState) {
                MainUiState.Loading -> TutTutLoadingScreen()
                is MainUiState.Success -> {
                    if (uiState.cropList.isEmpty()) {
                        NoResults(
                            modifier = Modifier.weight(1f),
                            announce = stringResource(id = R.string.crops_empty)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = screenHorizontalPadding),
                            state = scrollState,
                        ) {
                            items(
                                items = uiState.cropList,
                                key = { it.id }
                            ) { crops ->
                                CropsItem(
                                    crops = crops,
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
    crops: CropsDto,
    cropsInfoMap: HashMap<String, CropsInfoDto>,
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
                    if (crops.isHarvested) {
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