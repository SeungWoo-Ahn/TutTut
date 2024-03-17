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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.constant.CUSTOM_IMAGE
import io.tuttut.data.constant.CUSTOM_KEY
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.constant.DEFAULT_MAIN_IMAGE
import io.tuttut.data.model.dto.Diary
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.DeleteBottomSheet
import io.tuttut.presentation.ui.component.HarvestBottomSheet
import io.tuttut.presentation.ui.component.HarvestButton
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.RecipeItem
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.WateringButton
import io.tuttut.presentation.util.getDDay
import io.tuttut.presentation.util.getToday

@Composable
fun CropsDetailRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    moveCropsInfo: () -> Unit,
    moveEditCrops: () -> Unit,
    moveDiaryList: () -> Unit,
    onDiary: () -> Unit,
    moveAddDiary: () -> Unit,
    moveMain: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: CropsDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()

    when (uiState) {
        CropsDetailUiState.Loading -> TutTutLoadingScreen()
        is CropsDetailUiState.Success -> {
            CropsDetailScreen(
                modifier = modifier,
                crops = (uiState as CropsDetailUiState.Success).crops,
                cropsInfoMap = viewModel.cropsInfoMap,
                diaryList = listOf(),
                recipeUiState = recipeUiState,
                onBack = onBack,
                moveDiaryList = moveDiaryList,
                onHarvest = { viewModel.showHarvestDialog = true },
                moveCropsInfo = { viewModel.onMoveCropsInfo(it, moveCropsInfo) },
                onDiary = onDiary,
                onWatering = { viewModel.onWatering(it, onShowSnackBar) },
                moveAddDiary = moveAddDiary,
                onRecipe = {},
                onEdit = { viewModel.onEdit(it, moveEditCrops) },
                onDelete = { viewModel.showDeleteDialog = true }
            )
            DeleteBottomSheet(
                showSheet = viewModel.showDeleteDialog,
                onDelete = { viewModel.onDelete((uiState as CropsDetailUiState.Success).crops, moveMain, onShowSnackBar) },
                onDismissRequest = { viewModel.showDeleteDialog = false }
            )
            HarvestBottomSheet(
                showSheet = viewModel.showHarvestDialog,
                onHarvest = {
                    viewModel.onHarvest(
                        (uiState as CropsDetailUiState.Success).crops,
                        onShowSnackBar
                    )
                },
                onDismissRequest = { viewModel.showHarvestDialog = false }
            )
        }
    }
    BackHandler(onBack = onBack)
}

@Composable
internal fun CropsDetailScreen(
    modifier: Modifier,
    crops: Crops,
    cropsInfoMap: HashMap<String, CropsInfo>,
    diaryList: List<Diary>,
    recipeUiState: CropsRecipeUiState,
    onBack: () -> Unit,
    moveCropsInfo: (String) -> Unit,
    onHarvest: () -> Unit,
    moveDiaryList: () -> Unit,
    onDiary: () -> Unit,
    onWatering: (Crops) -> Unit,
    moveAddDiary: () -> Unit,
    onRecipe: () -> Unit,
    onEdit: (Crops) -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier.fillMaxSize()
    ) {
        TutTutTopBar(title = crops.name, onBack = onBack) {
            MenuDropDownButton(onEdit = { onEdit(crops) }, onDelete = onDelete)
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
                                modifier = Modifier.clickable { if (crops.key != CUSTOM_KEY) moveCropsInfo(crops.key) },
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
                        HarvestButton(isHarvested = crops.isHarvested, onClick = onHarvest)
                    }
                    Spacer(modifier = Modifier.height(42.dp))
                    Row(Modifier.fillMaxWidth()) {
                        CropsDetailItem(
                            modifier = Modifier.weight(1f),
                            label = stringResource(id = R.string.day_growing),
                            icon = painterResource(id = R.drawable.ic_shovel),
                            content = getDDay(crops.plantingDate, 0).let {
                                when {
                                    it == 0 -> "오늘"
                                    it < 0 -> "${-it + 1}일"
                                    else -> "재배 전"
                                }
                            }
                        )
                        CropsDetailItem(
                            modifier = Modifier.weight(1f),
                            label = stringResource(id = R.string.day_watering),
                            icon = painterResource(id = R.drawable.ic_water),
                            content = crops.wateringInterval?.let {
                                val dayDiff = getDDay(crops.lastWatered, it)
                                when {
                                    dayDiff == 0 -> "오늘"
                                    dayDiff > 0 -> "${dayDiff}일 후"
                                    else -> "-"
                                }
                            } ?: "-"
                        )
                        CropsDetailItem(
                            modifier = Modifier.weight(1f),
                            label = if (crops.isHarvested) stringResource(id = R.string.harvest_count) else stringResource(id = R.string.day_harvest),
                            icon = painterResource(id = R.drawable.ic_harvest),
                            content = if (crops.isHarvested) {
                                "${crops.harvestCnt} 번"
                            } else {
                                crops.growingDay?.let {
                                    val dayDiff = getDDay(crops.plantingDate, it)
                                    when {
                                        dayDiff == 0 -> "오늘"
                                        dayDiff > 0 -> "${dayDiff}일 후"
                                        else -> ""
                                    }
                                } ?: "-"
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(42.dp))
                    CropsLastInfoItem(
                        label = stringResource(id = R.string.day_last_watered),
                        content = getDDay(crops.lastWatered, 0).let {
                            when {
                                it == 0 -> "오늘"
                                else -> "${-it}일 전"
                            }
                        }

                    )
                    CropsLastInfoItem(
                        label = stringResource(id = R.string.watering_interval),
                        content = crops.wateringInterval?.let { "${it}일" } ?: "-"
                    )
                    if (crops.key == CUSTOM_KEY) {
                        CropsLastInfoItem(
                            label = stringResource(id = R.string.growing_day),
                            content = crops.growingDay?.let { "${it}일" } ?: "-"
                        )
                    }
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                CropsLabelButton(
                    title = stringResource(id = R.string.diary),
                    onClick = moveDiaryList
                )
            }
            if (crops.key != CUSTOM_KEY) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        CropsLabelButton(
                            title = "${crops.name} ${stringResource(id = R.string.crops_recipe)}",
                            onClick = moveDiaryList
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                when (recipeUiState) {
                    CropsRecipeUiState.Loading -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            TutTutLoadingScreen(Modifier.height(300.dp))
                        }
                    }
                    is CropsRecipeUiState.Success -> {
                        items(
                            count = recipeUiState.recipes.size,
                            key = { it }
                        ) { index ->
                            RecipeItem(
                                recipe = recipeUiState.recipes[index],
                                isLeftItem = index % 2 == 0,
                                onItemClick = onRecipe
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
        ) {
            WateringButton(
                isWatered = if (crops.wateringInterval == null) false else crops.lastWatered == getToday(),
                onClick = { onWatering(crops) }
            )
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

@Composable
fun CropsLastInfoItem(
    modifier: Modifier = Modifier,
    label: String,
    content: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenHorizontalPadding, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.surface
        )
        Text(
            text = content,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
fun CropsLabelButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = screenHorizontalPadding, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = R.drawable.ic_right),
            contentDescription = "ic-right"
        )
    }
}