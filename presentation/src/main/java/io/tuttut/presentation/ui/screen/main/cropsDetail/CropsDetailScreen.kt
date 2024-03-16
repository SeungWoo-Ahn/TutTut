package io.tuttut.presentation.ui.screen.main.cropsDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.tuttut.data.model.dto.CUSTOM_IMAGE
import io.tuttut.data.model.dto.CUSTOM_KEY
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.DEFAULT_MAIN_IMAGE
import io.tuttut.data.model.dto.Diary
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.HarvestButton
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.WateringButton
import io.tuttut.presentation.util.getCalcDayDiff
import io.tuttut.presentation.util.getDayDiff

@Composable
fun CropsDetailRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    moveCropsInfo: () -> Unit,
    moveDiaryList: () -> Unit,
    onDiary: () -> Unit,
    moveAddDiary: () -> Unit
) {
    CropsDetailScreen(
        modifier = modifier,
        crops = Crops(
            key = "potato",
            name = "감자",
            nickName = "소중한 감자",
            lastWatered = "2024-03-15",
            plantingDate = "2024-03-15",
            wateringInterval = 10,
            growingDay = 90
        ),
        diaryList = listOf(),
        cropsInfoMap = HashMap(),
        onBack = onBack,
        moveDiaryList = moveDiaryList,
        onHarvest = {},
        moveCropsInfo = moveCropsInfo,
        onDiary = onDiary,
        onWatering = {},
        moveAddDiary = moveAddDiary
    )
    BackHandler(onBack = onBack)
}

@Composable
fun CropsDetailScreen(
    modifier: Modifier,
    crops: Crops,
    diaryList: List<Diary>,
    cropsInfoMap: HashMap<String, CropsInfo>,
    onBack: () -> Unit,
    moveCropsInfo: () -> Unit,
    onHarvest: () -> Unit,
    moveDiaryList: () -> Unit,
    onDiary: () -> Unit,
    onWatering: () -> Unit,
    moveAddDiary: () -> Unit
) {
    Column(
        modifier.fillMaxSize()
    ) {
        TutTutTopBar(title = crops.name, onBack = onBack) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "ic-menu"
            )
        }
        LazyVerticalGrid(
            modifier = modifier.weight(1f),
            columns = GridCells.Fixed(2),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                    ) {
                        TutTutImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .align(Alignment.TopCenter),
                            url = crops.mainImgUrl ?: DEFAULT_MAIN_IMAGE
                        )
                        Row(
                            modifier = Modifier.align(Alignment.BottomStart)
                        ) {
                            Spacer(modifier = Modifier.width(screenHorizontalPadding))
                            TutTutImage(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                url = cropsInfoMap[crops.key]?.imageUrl ?: CUSTOM_IMAGE
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenHorizontalPadding),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                modifier = Modifier.clickable { moveCropsInfo() },
                                text = crops.name,
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp,
                                textDecoration = if (crops.key != CUSTOM_KEY) TextDecoration.Underline else null
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = crops.nickName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 20.sp
                            )
                        }
                        HarvestButton(onClick = onHarvest)
                    }
                    Spacer(modifier = Modifier.height(42.dp))
                    Row(Modifier.fillMaxWidth()) {
                        val growingDiff = getDayDiff(crops.plantingDate)
                        CropsDetailItem(
                            modifier = Modifier.weight(1f),
                            label = stringResource(id = R.string.day_growing),
                            icon = painterResource(id = R.drawable.ic_shovel),
                            content = when {
                                growingDiff == 0 -> "오늘"
                                growingDiff > 0 -> "${growingDiff}일"
                                else -> "재배 전"
                            }
                        )
                        CropsDetailItem(
                            modifier = Modifier.weight(1f),
                            label = stringResource(id = R.string.day_watering),
                            icon = painterResource(id = R.drawable.ic_water),
                            content = crops.wateringInterval?.let {
                                val dayDiff = getDayDiff(crops.lastWatered)
                                when {
                                    dayDiff == 0 -> "오늘"
                                    dayDiff < 0 -> "${-dayDiff}일 전"
                                    else -> "-"
                                }
                            } ?: "-"
                        )
                        CropsDetailItem(
                            modifier = Modifier.weight(1f),
                            label = stringResource(id = R.string.day_harvest),
                            icon = painterResource(id = R.drawable.ic_harvest),
                            content = crops.growingDay?.let {
                                val dayDiff = getCalcDayDiff(crops.plantingDate, it)
                                when {
                                    dayDiff == 0 -> "오늘"
                                    dayDiff > 0 -> "${dayDiff}일 후"
                                    else -> ""
                                }
                            } ?: "-"
                        )
                    }
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
        ) {
            WateringButton(onClick = onWatering)
            Spacer(modifier = Modifier.width(12.dp))
            TutTutButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.write_diary),
                isLoading = false,
                onClick = moveAddDiary
            )
        }
    }
}

@Composable
fun CropsDetailItem(
    modifier: Modifier,
    label: String,
    icon: Painter,
    content: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(95.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            modifier = Modifier.size(30.dp),
            painter = icon,
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = "ic-item"
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = content,
            style = MaterialTheme.typography.labelLarge
        )
    }
}