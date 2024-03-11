package io.tuttut.presentation.ui.screen.main.recommend

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Difficulty
import io.tuttut.data.model.dto.Season
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.CropsInfoScreenPart
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.screen.main.imageUrl

val cropsInfoList = listOf(
    CropsInfo(
        key = "tomato",
        name = "토마토",
        imageUrl = imageUrl,
        difficulty = Difficulty.EASY,
        plantingSeasons = listOf(Season(3, 7)),
        plantingInterval = "한 달",
        wateringInterval = 2,
        wateringIntervalStr = "매주",
        harvestSeasons = listOf(Season(6, 10))
    ),
    CropsInfo(
        key = "potato",
        name = "감자",
        imageUrl = imageUrl,
        difficulty = Difficulty.MEDIUM,
        plantingSeasons = listOf(Season(4, 6)),
        plantingInterval = "두 달",
        wateringInterval = 3,
        wateringIntervalStr = "매주",
        harvestSeasons = listOf(Season(7, 9))
    ),
    CropsInfo(
        key = "cucumber",
        name = "오이",
        imageUrl = imageUrl,
        difficulty = Difficulty.EASY,
        plantingSeasons = listOf(Season(5, 8)),
        plantingInterval = "한 달",
        wateringInterval = 2,
        wateringIntervalStr = "매주",
        harvestSeasons = listOf(Season(7, 10))
    ),
    CropsInfo(
        key = "carrot",
        name = "당근",
        imageUrl = imageUrl,
        difficulty = Difficulty.EASY,
        plantingSeasons = listOf(Season(3, 5)),
        plantingInterval = "한 달",
        wateringInterval = 2,
        wateringIntervalStr = "매주",
        harvestSeasons = listOf(Season(6, 8))
    ),
    CropsInfo(
        key = "lettuce",
        name = "상추",
        imageUrl = imageUrl,
        difficulty = Difficulty.EASY,
        plantingSeasons = listOf(Season(2, 6)),
        plantingInterval = "세 달",
        wateringInterval = 1,
        wateringIntervalStr = "매일",
        harvestSeasons = listOf(Season(4, 8))
    )
)

@Composable
fun SelectCropsRoute(modifier: Modifier = Modifier, onBack: () -> Unit, onItemClick: (String) -> Unit, onButton: () -> Unit) {
    SelectCropsScreen(
        modifier = modifier,
        onBack = onBack,
        onItemClick = onItemClick,
        onButton = onButton
    )
    BackHandler(onBack = onBack)
}

@Composable
fun SelectCropsScreen(modifier: Modifier, onBack: () -> Unit, onItemClick: (String) -> Unit, onButton: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = stringResource(id = R.string.select_crops),
            needBack = true,
            onBack = onBack
        )
        CropsInfoScreenPart(
            modifier = modifier.weight(1f),
            monthlyCrops = emptyList(),
            totalCrops = cropsInfoList,
            onItemClick = onItemClick
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            TutTutButton(
                text = stringResource(id = R.string.select_myself),
                isLoading = false,
                onClick = onButton
            )
        }
    }
}
