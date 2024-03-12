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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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

val cropsList = listOf(
    Crops(
        id = "1",
        key = "tomato",
        nickName = "토마토마톰",
        wateringInterval = 2,
        growingDay = 30,
        lastWatered = "2024-03-05",
        plantingDay = "2024-02-05",
        diaryCnt = 3,
        isHarvested = false
    ),
    Crops(
        id = "2",
        key = "cucumber",
        nickName = "애플애플",
        wateringInterval = 3,
        growingDay = 50,
        lastWatered = "2024-03-08",
        plantingDay = "2024-01-20",
        diaryCnt = 2,
        isHarvested = false
    ),
    Crops(
        id = "3",
        key = "pumpkin",
        nickName = "당근마켓",
        wateringInterval = 1,
        growingDay = 40,
        lastWatered = "2024-03-10",
        plantingDay = "2024-02-01",
        diaryCnt = 1,
        isHarvested = false
    ),
    Crops(
        id = "4",
        key = "radish",
        nickName = "바질은 바질바질",
        wateringInterval = 2,
        growingDay = 20,
        lastWatered = "2024-03-06",
        plantingDay = "2024-02-14",
        diaryCnt = 0,
        isHarvested = false
    ),
    Crops(
        id = "5",
        key = "potato",
        nickName = "딸기가 좋아",
        wateringInterval = 2,
        growingDay = 25,
        lastWatered = "2024-03-09",
        plantingDay = "2024-01-25",
        diaryCnt = 2,
        isHarvested = false
    ),
)

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    moveRecommend: () -> Unit,
    moveMy: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val selectedTab by viewModel.selectedTab
    val cropsInfoMap = viewModel.cropsInfoRepo.cropsInfoMap
    MainScreen(
        modifier = modifier,
        uiState = uiState,
        gardenName = viewModel.gardenName,
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
    gardenName: String,
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
            TutTutTopBar(title = gardenName, needBack = false) {
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
                MainUiState.Nothing -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenHorizontalPadding),
                        state = scrollState
                    ) {
                        items(
                            items = cropsList,
                            key = { it.id },
                        ) {
                            CropsItem(
                                crops = it,
                                isHarvested = selectedTab == MainTab.HARVESTED,
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
                                text = getDDay(crops.plantingDay, crops.growingDay),
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