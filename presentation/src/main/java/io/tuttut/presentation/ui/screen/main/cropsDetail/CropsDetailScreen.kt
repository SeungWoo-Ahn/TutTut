package io.tuttut.presentation.ui.screen.main.cropsDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
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
import io.tuttut.data.model.dto.User
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.util.withScreenPadding
import io.tuttut.presentation.ui.component.NegativeBottomSheet
import io.tuttut.presentation.ui.component.HarvestBottomSheet
import io.tuttut.presentation.ui.component.HarvestButton
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.RecipeItem
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.WateringButton
import io.tuttut.presentation.ui.component.loading
import io.tuttut.presentation.util.getDDay
import io.tuttut.presentation.util.getToday
import kotlinx.coroutines.CoroutineScope

@Composable
fun CropsDetailRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    onBack: () -> Unit,
    moveCropsInfo: () -> Unit,
    moveEditCrops: () -> Unit,
    moveDiaryList: () -> Unit,
    moveDiaryDetail: () -> Unit,
    moveAddDiary: () -> Unit,
    moveMain: () -> Unit,
    moveRecipeWeb: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: CropsDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val diaryUiState by viewModel.diaryUiState.collectAsStateWithLifecycle()
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()

    when (uiState) {
        CropsDetailUiState.Loading -> TutTutLoadingScreen()
        is CropsDetailUiState.Success -> {
            val crops = (uiState as CropsDetailUiState.Success).crops
            CropsDetailScreen(
                modifier = modifier,
                crops = crops,
                cropsInfoMap = viewModel.cropsInfoMap,
                memberMap = viewModel.gardenMemberMap,
                diaryUiState = diaryUiState,
                recipeUiState = recipeUiState,
                onBack = onBack,
                moveDiaryList = moveDiaryList,
                onDiary = { viewModel.onDiary(it, moveDiaryDetail) },
                moveAddDiary = { viewModel.onAddDiary(moveAddDiary) },
                onRecipe = { viewModel.onRecipe(it, moveRecipeWeb) },
                onHarvest = { viewModel.showHarvestDialog = true },
                moveCropsInfo = { viewModel.onMoveCropsInfo(moveCropsInfo) },
                onWatering = { viewModel.onWatering(onShowSnackBar) },
                onEdit = { viewModel.onEdit(moveEditCrops) },
                onDelete = { viewModel.showDeleteDialog = true }
            )
            NegativeBottomSheet(
                showSheet = viewModel.showDeleteDialog,
                scope = scope,
                onButton = { viewModel.onDelete(moveMain, onShowSnackBar) },
                onDismissRequest = { viewModel.showDeleteDialog = false }
            )
            HarvestBottomSheet(
                showSheet = viewModel.showHarvestDialog,
                scope = scope,
                onHarvest = { viewModel.onHarvest(onShowSnackBar) },
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
    memberMap: HashMap<String, User>,
    diaryUiState: CropsDiaryUiState,
    recipeUiState: CropsRecipeUiState,
    onDiary: (Diary) -> Unit,
    onRecipe: (String) -> Unit,
    moveCropsInfo: () -> Unit,
    onEdit: () -> Unit,
    onWatering: () -> Unit,
    onBack: () -> Unit,
    onHarvest: () -> Unit,
    moveDiaryList: () -> Unit,
    moveAddDiary: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier.fillMaxSize()
    ) {
        TutTutTopBar(title = crops.name, onBack = onBack) {
            MenuDropDownButton(
                isMine = true,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2),
        ) {
            cropsDetail(
                crops = crops,
                cropsInfoMap = cropsInfoMap,
                moveCropsInfo = moveCropsInfo,
                onHarvest = onHarvest
            )
            cropsDetailDiary(
                diaryUiState = diaryUiState,
                memberMap = memberMap,
                moveDiaryList = moveDiaryList,
                onDiary = onDiary
            )
            cropsDetailRecipe(
                recipeUiState = recipeUiState,
                crops = crops,
                onRecipe = onRecipe
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
        ) {
            WateringButton(
                isWatered = if (crops.wateringInterval == null) false else crops.lastWatered == getToday(),
                onClick = onWatering
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
internal fun LazyGridScope.cropsDetail(
    modifier: Modifier = Modifier,
    crops: Crops,
    cropsInfoMap: HashMap<String, CropsInfo>,
    moveCropsInfo: () -> Unit,
    onHarvest: () -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column(modifier) {
            CropsDetailHeader(crops = crops, cropsInfoMap = cropsInfoMap)
            Spacer(modifier = Modifier.height(24.dp))
            CropsDetailName(crops = crops, moveCropsInfo = moveCropsInfo, onHarvest = onHarvest)
            Spacer(modifier = Modifier.height(42.dp))
            CropsDetailBody(crops = crops)
            Spacer(modifier = Modifier.height(42.dp))
            CropsDetailFooter(crops = crops)
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}



internal fun LazyGridScope.cropsDetailDiary(
    diaryUiState: CropsDiaryUiState,
    memberMap: HashMap<String, User>,
    onDiary: (Diary) -> Unit,
    moveDiaryList: () -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        CropsLabelButton(
            title = stringResource(id = R.string.diary),
            onClick = moveDiaryList
        )
    }
    when (diaryUiState) {
        CropsDiaryUiState.Loading -> loading(300)
        is CropsDiaryUiState.Success -> {
            itemsIndexed(
                items = diaryUiState.diaryList,
                key = { _, it -> it.id },
            ) { index, item ->
                CropsDiaryItem(
                    diary = item,
                    isLeftItem = index % 2 == 0,
                    memberMap = memberMap,
                    onItemClick = { onDiary(item) }
                )
            }
        }
    }
}

internal fun LazyGridScope.cropsDetailRecipe(
    recipeUiState: CropsRecipeUiState,
    crops: Crops,
    onRecipe: (String) -> Unit
) {
    if (crops.key != CUSTOM_KEY) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            CropsLabelButton(
                title = "${crops.name} ${stringResource(id = R.string.crops_recipe)}",
                onClick = { onRecipe("/recipe/list.html?q=${crops.name}") }
            )
        }
        when (recipeUiState) {
            CropsRecipeUiState.Loading -> loading(300)
            is CropsRecipeUiState.Success -> {
                itemsIndexed(
                    items = recipeUiState.recipes,
                    key = { index, _ -> index }
                ) { index, item ->
                    RecipeItem(
                        recipe = item,
                        isLeftItem = index % 2 == 0,
                        onItemClick = { onRecipe(item.link) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun CropsDetailHeader(
    modifier: Modifier = Modifier,
    crops: Crops,
    cropsInfoMap: HashMap<String, CropsInfo>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {
        TutTutImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .align(Alignment.TopCenter),
            url = crops.mainImg?.url ?: DEFAULT_MAIN_IMAGE
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
}

@Composable
internal fun CropsDetailName(
    modifier: Modifier = Modifier,
    crops: Crops,
    moveCropsInfo: () -> Unit,
    onHarvest: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenHorizontalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                modifier = Modifier.clickable { if (crops.key != CUSTOM_KEY) moveCropsInfo() },
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
}

@Composable
internal fun CropsDetailBody(
    modifier: Modifier = Modifier,
    crops: Crops
) {
    Row(modifier.fillMaxWidth()) {
        CropsDetailItem(
            modifier = Modifier.weight(1f),
            label = stringResource(id = R.string.day_growing),
            icon = painterResource(id = R.drawable.ic_shovel),
            content = getDDay(crops.plantingDate, 0).let { day ->
                when {
                    day < 0 -> "${-day + 1}일"
                    day < 1 -> "오늘"
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
                    dayDiff > 0 -> "${dayDiff}일 후"
                    else -> "오늘"
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
                        else -> "${-dayDiff} 지남"
                    }
                } ?: "-"
            }
        )
    }
}

@Composable
internal fun CropsDetailFooter(
    crops: Crops
) {
    CropsLastInfoItem(
        label = stringResource(id = R.string.day_last_watered),
        content = getDDay(crops.lastWatered, 0).let { day ->
            when {
                day == 0 -> "오늘"
                else -> "${-day}일 전"
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
}

@Composable
internal fun CropsDetailItem(
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
internal fun CropsLastInfoItem(
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
internal fun CropsLabelButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = screenHorizontalPadding, vertical = 26.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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

@Composable
internal fun CropsDiaryItem(
    modifier: Modifier = Modifier,
    diary: Diary,
    isLeftItem: Boolean,
    memberMap: HashMap<String, User>,
    onItemClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (isLeftItem) screenHorizontalPadding else 8.dp,
                end = if (!isLeftItem) screenHorizontalPadding else 8.dp,
                bottom = 24.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onItemClick
                )
        ) {
            TutTutImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(MaterialTheme.shapes.medium),
                url = if (diary.imgUrlList.isNotEmpty()) diary.imgUrlList[0].url else DEFAULT_MAIN_IMAGE
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = diary.content,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = memberMap[diary.authorId]?.name
                    ?: stringResource(id = R.string.unknown_user),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}