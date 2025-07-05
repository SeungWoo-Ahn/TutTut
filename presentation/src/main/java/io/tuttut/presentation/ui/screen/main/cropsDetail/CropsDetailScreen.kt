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
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.cropsInfo.Recipe
import io.tuttut.presentation.R
import io.tuttut.presentation.model.DetailCropsUiModel
import io.tuttut.presentation.model.DetailDiaryUiModel
import io.tuttut.presentation.model.WateringState
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.HarvestBottomSheet
import io.tuttut.presentation.ui.component.HarvestButton
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.NegativeBottomSheet
import io.tuttut.presentation.ui.component.RecipeItem
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.WateringButton
import io.tuttut.presentation.util.withScreenPadding
import kotlinx.coroutines.CoroutineScope

@Composable
fun CropsDetailRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    moveCropsInfo: (CropsKey, String) -> Unit,
    moveEditCrops: () -> Unit,
    moveDiaryList: (String) -> Unit,
    moveDiaryDetail: (String) -> Unit,
    moveAddDiary: () -> Unit,
    moveMain: () -> Unit,
    moveRecipeWeb: (String, String) -> Unit,
    onBack: () -> Unit,
    viewModel: CropsDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        CropsDetailUiState.Loading -> TutTutLoadingScreen()
        is CropsDetailUiState.Success -> {
            CropsDetailScreen(
                modifier = modifier,
                uiState = uiState as CropsDetailUiState.Success,
                moveCropsInfo = moveCropsInfo,
                moveDiaryList = moveDiaryList,
                moveAddDiary = moveAddDiary,
                onDiary = moveDiaryDetail,
                onRecipe = moveRecipeWeb,
                onEdit = moveEditCrops,
                onWatering = viewModel::onWatering,
                onHarvest = { viewModel.setHarvestDialogState(true) },
                onDelete = { viewModel.setDeleteDialogState(true) },
                onBack = onBack,
            )
            NegativeBottomSheet(
                showSheet = viewModel.showDeleteDialog,
                scope = scope,
                onButton = { viewModel.onDelete(moveMain) },
                onDismissRequest = { viewModel.setDeleteDialogState(false) }
            )
            HarvestBottomSheet(
                showSheet = viewModel.showHarvestDialog,
                scope = scope,
                onHarvest = viewModel::onHarvest,
                onDismissRequest = { viewModel.setHarvestDialogState(false) }
            )
        }
    }
    BackHandler(onBack = onBack)
}

@Composable
private fun CropsDetailScreen(
    modifier: Modifier,
    uiState: CropsDetailUiState.Success,
    moveCropsInfo: (CropsKey, String) -> Unit,
    moveDiaryList: (String) -> Unit,
    moveAddDiary: () -> Unit,
    onDiary: (String) -> Unit,
    onRecipe: (String, String) -> Unit,
    onEdit: () -> Unit,
    onWatering: (WateringState) -> Unit,
    onHarvest: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier.fillMaxSize()
    ) {
        TutTutTopBar(title = uiState.crops.name, onBack = onBack) {
            MenuDropDownButton(
                isMine = true,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            cropsDetail(
                crops = uiState.crops,
                moveCropsInfo = { moveCropsInfo(uiState.crops.key, uiState.crops.name) },
                onHarvest = onHarvest
            )
            cropsDetailDiary(
                diaryList = uiState.diaryList,
                moveDiaryList = { moveDiaryList(uiState.crops.name) },
                onDiary = onDiary
            )
            if (uiState.crops.key != CropsKey.CUSTOM) {
                cropsDetailRecipe(
                    recipeList = uiState.recipeList,
                    cropsName = uiState.crops.name,
                    onRecipe = { link -> onRecipe(uiState.crops.name, link) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
        ) {
            WateringButton(
                isWatered = uiState.crops.wateringState != WateringState.POSSIBLE,
                onClick = { onWatering(uiState.crops.wateringState) }
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

private fun LazyGridScope.cropsDetail(
    modifier: Modifier = Modifier,
    crops: DetailCropsUiModel,
    moveCropsInfo: () -> Unit,
    onHarvest: () -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column(modifier) {
            CropsDetailHeader(crops = crops)
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



private fun LazyGridScope.cropsDetailDiary(
    diaryList: List<DetailDiaryUiModel>,
    moveDiaryList: () -> Unit,
    onDiary: (String) -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        CropsLabelButton(
            title = stringResource(id = R.string.diary),
            onClick = moveDiaryList
        )
    }
    itemsIndexed(
        items = diaryList,
        key = { _, it -> it.id },
    ) { index, diary ->
        CropsDiaryItem(
            diary = diary,
            isLeftItem = index % 2 == 0,
            onItemClick = { onDiary(diary.id) }
        )
    }
}

private fun LazyGridScope.cropsDetailRecipe(
    recipeList: List<Recipe>,
    cropsName: String,
    onRecipe: (String) -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        CropsLabelButton(
            title = "$cropsName ${stringResource(id = R.string.crops_recipe)}",
            onClick = { onRecipe("/recipe/list.html?q=${cropsName}") }
        )
    }
    itemsIndexed(
        items = recipeList,
        key = { index, _ -> index }
    ) { index, recipe ->
        RecipeItem(
            recipe = recipe,
            isLeftItem = index % 2 == 0,
            onItemClick = { onRecipe(recipe.link) }
        )
    }
}

@Composable
private fun CropsDetailHeader(
    modifier: Modifier = Modifier,
    crops: DetailCropsUiModel,
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
            url = crops.mainImageUrl
        )
        Row(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Spacer(modifier = Modifier.width(screenHorizontalPadding))
            TutTutImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                url = crops.imageUrl
            )
        }
    }
}

@Composable
private fun CropsDetailName(
    modifier: Modifier = Modifier,
    crops: DetailCropsUiModel,
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
                modifier = Modifier.clickable { if (crops.key != CropsKey.CUSTOM) moveCropsInfo() },
                text = crops.name,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                textDecoration = if (crops.key != CropsKey.CUSTOM) TextDecoration.Underline else null
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
private fun CropsDetailBody(
    modifier: Modifier = Modifier,
    crops: DetailCropsUiModel,
) {
    Row(modifier.fillMaxWidth()) {
        CropsDetailItem(
            modifier = Modifier.weight(1f),
            label = stringResource(id = R.string.day_growing),
            icon = painterResource(id = R.drawable.ic_shovel),
            content = crops.plantingDay
        )
        CropsDetailItem(
            modifier = Modifier.weight(1f),
            label = stringResource(id = R.string.day_watering),
            icon = painterResource(id = R.drawable.ic_water),
            content = crops.wateringDay
        )
        CropsDetailItem(
            modifier = Modifier.weight(1f),
            label = if (crops.isHarvested) stringResource(id = R.string.harvest_count) else stringResource(id = R.string.day_harvest),
            icon = painterResource(id = R.drawable.ic_harvest),
            content = if (crops.isHarvested) crops.harvest else crops.harvestDay,
        )
    }
}

@Composable
private fun CropsDetailFooter(
    crops: DetailCropsUiModel,
) {
    CropsLastInfoItem(
        label = stringResource(id = R.string.day_last_watered),
        content = crops.lastWateredDay

    )
    CropsLastInfoItem(
        label = stringResource(id = R.string.watering_interval),
        content = crops.wateringInterval
    )
    CropsLastInfoItem(
        label = stringResource(id = R.string.growing_day),
        content = crops.growingDay
    )
}

@Composable
private fun CropsDetailItem(
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
private fun CropsLastInfoItem(
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
private fun CropsLabelButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
private fun CropsDiaryItem(
    modifier: Modifier = Modifier,
    diary: DetailDiaryUiModel,
    isLeftItem: Boolean,
    onItemClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (isLeftItem) screenHorizontalPadding else 0.dp,
                end = if (isLeftItem.not()) screenHorizontalPadding else 0.dp,
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
                url = diary.firstImageUrl
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
                text = diary.authorName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}