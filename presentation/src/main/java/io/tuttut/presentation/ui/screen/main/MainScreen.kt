package io.tuttut.presentation.ui.screen.main

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.data.model.dto.Crops
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.TutTutFAB
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.getDDay

val imageUrl = "data:image/jpeg;base64,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"

val cropsList = listOf(
    Crops(
        id = "1",
        type = "Vegetable",
        name = "토마토",
        nickName = "토마토마톰",
        imageUrl = imageUrl,
        wateringGap = 2,
        growingDay = 30,
        lastWatered = "2024-03-05",
        plantingDay = "2024-02-05",
        diaryCnt = 3,
        isHarvested = true
    ),
    Crops(
        id = "2",
        type = "Fruit",
        name = "사과",
        nickName = "애플애플",
        imageUrl = imageUrl,
        wateringGap = 3,
        growingDay = 50,
        lastWatered = "2024-03-08",
        plantingDay = "2024-01-20",
        diaryCnt = 2,
        isHarvested = false
    ),
    Crops(
        id = "3",
        type = "Vegetable",
        name = "당근",
        nickName = "당근마켓",
        imageUrl = imageUrl,
        wateringGap = 1,
        growingDay = 40,
        lastWatered = "2024-03-10",
        plantingDay = "2024-02-01",
        diaryCnt = 1,
        isHarvested = true
    ),
    Crops(
        id = "4",
        type = "Herb",
        name = "바질",
        nickName = "바질은 바질바질",
        imageUrl = imageUrl,
        wateringGap = 2,
        growingDay = 20,
        lastWatered = "2024-03-06",
        plantingDay = "2024-02-14",
        diaryCnt = 0,
        isHarvested = false
    ),
    Crops(
        id = "5",
        type = "Fruit",
        name = "딸기",
        nickName = "딸기가 좋아",
        imageUrl = imageUrl,
        wateringGap = 2,
        growingDay = 25,
        lastWatered = "2024-03-09",
        plantingDay = "2024-01-25",
        diaryCnt = 2,
        isHarvested = true
    ),
    Crops(
        id = "6",
        type = "Vegetable",
        name = "토마토",
        nickName = "토마토마톰",
        imageUrl = imageUrl,
        wateringGap = 2,
        growingDay = 30,
        lastWatered = "2024-03-05",
        plantingDay = "2024-02-05",
        diaryCnt = 3,
        isHarvested = true
    ),
    Crops(
        id = "7",
        type = "Fruit",
        name = "사과",
        nickName = "애플애플",
        imageUrl = imageUrl,
        wateringGap = 3,
        growingDay = 50,
        lastWatered = "2024-03-08",
        plantingDay = "2024-01-20",
        diaryCnt = 2,
        isHarvested = false
    ),
    Crops(
        id = "8",
        type = "Vegetable",
        name = "당근",
        nickName = "당근마켓",
        imageUrl = imageUrl,
        wateringGap = 1,
        growingDay = 40,
        lastWatered = "2024-03-10",
        plantingDay = "2024-02-01",
        diaryCnt = 1,
        isHarvested = true
    ),
    Crops(
        id = "9",
        type = "Herb",
        name = "바질",
        nickName = "바질은 바질바질",
        imageUrl = imageUrl,
        wateringGap = 2,
        growingDay = 20,
        lastWatered = "2024-03-06",
        plantingDay = "2024-02-14",
        diaryCnt = 0,
        isHarvested = false
    ),
    Crops(
        id = "10",
        type = "Fruit",
        name = "딸기",
        nickName = "딸기가 좋아",
        imageUrl = imageUrl,
        wateringGap = 2,
        growingDay = 25,
        lastWatered = "2024-03-09",
        plantingDay = "2024-01-25",
        diaryCnt = 2,
        isHarvested = true
    )
)

@Composable
fun MainRoute(modifier: Modifier = Modifier) {
    MainScreen(modifier = modifier)
}

@Composable
fun MainScreen(modifier: Modifier) {
    val scrollState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TutTutTopBar(title = "텃텃텃밭", needBack = false) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "user-icon"
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenHorizontalPadding),
                state = scrollState
            ) {
                items(
                    items = cropsList,
                    key = { it.id },
                    itemContent = { CropsItem(crops = it) }
                )
            }
        }
        TutTutFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = screenHorizontalPadding),
            text = stringResource(id = R.string.add),
            expanded = !scrollState.isScrollInProgress,
            onClick = {}
        )
    }
}

@Composable
fun CropsItem(modifier: Modifier = Modifier, crops: Crops) {
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
                url = crops.imageUrl
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
                                text = getDDay(crops.lastWatered, crops.wateringGap),
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