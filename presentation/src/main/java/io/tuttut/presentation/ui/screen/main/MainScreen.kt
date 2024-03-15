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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import io.tuttut.presentation.util.getDDay

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    moveRecommend: () -> Unit,
    moveMy: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.cachingGardenInfo()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val cropsInfoMap = viewModel.cropsInfoRepo.cropsInfoMap
    MainScreen(
        modifier = modifier,
        uiState = uiState,
        topBarState = topBarState,
        selectedTab = selectedTab,
        cropsInfoMap = cropsInfoMap,
        onTab = viewModel::onTab,
        moveRecommend = moveRecommend,
        moveMy = moveMy
    )
}

@Composable
internal fun MainScreen(
    modifier: Modifier,
    uiState: MainUiState,
    topBarState: MainTopBarState,
    selectedTab: MainTab,
    cropsInfoMap: HashMap<String, CropsInfo>,
    onTab: (MainTab) -> Unit,
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
                        .clickable { moveMy() },
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenHorizontalPadding),
                        state = scrollState
                    ) {
                        items(
                            items = uiState.cropsList,
                            key = { it.id },
                        ) {
                            CropsItem(
                                crops = it,
                                isHarvested = selectedTab.isHarvested,
                                cropsInfoMap = cropsInfoMap
                            )
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
    cropsInfoMap: HashMap<String, CropsInfo>
) {
    Column(modifier) {
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
                url = cropsInfoMap[crops.key]?.imageUrl ?: ""
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
                                text = crops.wateringInterval?.let { getDDay(crops.lastWatered, it) } ?: "-",
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
                                text = crops.growingDay?.let { getDDay(crops.plantingDate, it) } ?: "-",
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