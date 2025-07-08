package io.tuttut.presentation.ui.screen.main.cropsDetail

import io.tuttut.domain.model.cropsInfo.Recipe
import io.tuttut.presentation.model.DetailCropsUiModel
import io.tuttut.presentation.model.DetailDiaryUiModel

sealed interface CropsDetailUiState {
    data object Loading : CropsDetailUiState

    data class Success(
        val crops: DetailCropsUiModel,
        val diaryList: List<DetailDiaryUiModel>,
        val recipeList: List<Recipe>
    ) : CropsDetailUiState
}

