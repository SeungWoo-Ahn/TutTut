package io.tuttut.presentation.ui.screen.main.cropsDetail

import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.Recipe

sealed interface CropsDetailUiState {
    data object Loading : CropsDetailUiState
    data class Success(
        val crops: Crops
    ) : CropsDetailUiState
}

sealed interface CropsRecipeUiState {
    data object Loading : CropsRecipeUiState
    data class Success(
        val recipes: List<Recipe>
    ) : CropsRecipeUiState
}