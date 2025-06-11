package io.tuttut.presentation.ui.screen.main.cropsDetail

import io.tuttut.data.network.model.CropsDto
import io.tuttut.data.network.model.DiaryDto
import io.tuttut.data.network.model.RecipeDto

sealed interface CropsDetailUiState {
    data object Loading : CropsDetailUiState
    data class Success(
        val crops: CropsDto
    ) : CropsDetailUiState
}

sealed interface CropsDiaryUiState {
    data object Loading : CropsDiaryUiState
    data class Success(
        val diaryList: List<DiaryDto>
    ) : CropsDiaryUiState
}

sealed interface CropsRecipeUiState {
    data object Loading : CropsRecipeUiState
    data class Success(
        val recipes: List<RecipeDto>
    ) : CropsRecipeUiState
}