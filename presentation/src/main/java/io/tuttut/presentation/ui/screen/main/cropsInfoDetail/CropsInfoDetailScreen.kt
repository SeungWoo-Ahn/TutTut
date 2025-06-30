package io.tuttut.presentation.ui.screen.main.cropsInfoDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.domain.model.cropsInfo.Recipe
import io.tuttut.presentation.R
import io.tuttut.presentation.model.CropsInfoUiModel
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.util.withScreenPadding
import io.tuttut.presentation.ui.component.CropsInfoItem
import io.tuttut.presentation.ui.component.RecipeItem
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar

@Composable
fun CropsInfoDetailRoute(
    modifier: Modifier = Modifier,
    moveRecipeWeb: (String, String) -> Unit,
    moveAdd: () -> Unit,
    onBack: () -> Unit,
    viewModel: CropsInfoDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CropsInfoDetailScreen(
        modifier = modifier,
        uiState = uiState,
        readOnly = viewModel.readOnly,
        onRecipe = moveRecipeWeb,
        onButton = moveAdd,
        onBack = onBack,
    )
    BackHandler(onBack = onBack)
}

@Composable
private fun CropsInfoDetailScreen(
    modifier: Modifier,
    uiState: CropsInfoDetailUiState,
    readOnly: Boolean,
    onRecipe: (String, String) -> Unit,
    onButton: () -> Unit,
    onBack: () -> Unit,
) {
    when (uiState) {
        CropsInfoDetailUiState.Loading -> TutTutLoadingScreen()
        is CropsInfoDetailUiState.Success -> {
            Column(modifier.fillMaxSize()) {
                TutTutTopBar(
                    title = uiState.cropsInfo.name,
                    needBack = true,
                    onBack = onBack
                )
                LazyVerticalGrid(
                    modifier = Modifier.weight(1f),
                    columns = GridCells.Fixed(2)
                ) {
                    cropsInfo(cropsInfo = uiState.cropsInfo)
                    if (readOnly.not()) {
                        recipeList(
                            cropsInfoName = uiState.cropsInfo.name,
                            recipeList = uiState.recipeList,
                            onRecipe = onRecipe
                        )
                    }
                }
                if (readOnly.not()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .withScreenPadding()
                            .padding(top = 10.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        TutTutButton(
                            text = "${uiState.cropsInfo.name} ${stringResource(id = R.string.add)}",
                            isLoading = false,
                            onClick = onButton
                        )
                    }
                }
            }
        }
    }
}

private fun LazyGridScope.cropsInfo(
    modifier: Modifier = Modifier,
    cropsInfo: CropsInfoUiModel,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column(
            modifier = modifier.padding(screenHorizontalPadding)
        ) {
            Text(
                text = stringResource(id = R.string.crops_info),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CropsInfoItem(
                iconId = R.drawable.ic_trophy,
                nameId = R.string.difficulty,
                content = cropsInfo.difficulty
            )
            CropsInfoItem(
                iconId = R.drawable.ic_shovel,
                nameId = R.string.planting,
                content = cropsInfo.plantingSeasons
            )
            CropsInfoItem(
                iconId = R.drawable.ic_gap,
                nameId = R.string.planting_interval,
                content = cropsInfo.plantingInterval
            )
            CropsInfoItem(
                iconId = R.drawable.ic_water,
                nameId = R.string.watering,
                content = cropsInfo.wateringInterval
            )
            CropsInfoItem(
                iconId = R.drawable.ic_harvest,
                nameId = R.string.harvesting,
                content = cropsInfo.harvestSeasons
            )
        }
    }
}

private fun LazyGridScope.recipeList(
    cropsInfoName: String,
    recipeList: List<Recipe>,
    onRecipe: (String, String) -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column(
            modifier = Modifier.padding(horizontal = screenHorizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(54.dp))
            Text(
                text = "$cropsInfoName ${stringResource(id = R.string.crops_recipe)}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    items(
        count = recipeList.size,
        key = { it }
    ) { index ->
        RecipeItem(
            recipe = recipeList[index],
            isLeftItem = index % 2 == 0,
            onItemClick = {
                onRecipe(cropsInfoName, recipeList[index].link)
            }
        )
    }
}