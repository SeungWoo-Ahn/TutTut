package io.tuttut.presentation.ui.screen.main.cropsInfoDetail

import io.tuttut.domain.model.cropsInfo.Recipe
import io.tuttut.presentation.model.CropsInfoUiModel

sealed interface CropsInfoDetailUiState {
    data object Loading : CropsInfoDetailUiState

    data class Success(
        val cropsInfo: CropsInfoUiModel,
        val recipeList: List<Recipe>,
    ) : CropsInfoDetailUiState
}