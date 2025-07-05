package io.tuttut.presentation.ui.screen.main.cropsInfoDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.cropsInfo.Recipe
import io.tuttut.domain.usecase.cropsInfo.GetCropsInfoByKeyUseCase
import io.tuttut.domain.usecase.cropsInfo.GetCropsRecipeFlowUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.toUiModel
import io.tuttut.presentation.navigation.MainScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CropsInfoDetailViewModel @Inject constructor(
    private val getCropsInfoByKeyUseCase: GetCropsInfoByKeyUseCase,
    getCropsRecipeFlowUseCase: GetCropsRecipeFlowUseCase,
    savedStateHandle: SavedStateHandle,
): BaseViewModel() {
    private val route = savedStateHandle.toRoute<MainScreen.CropsInfoDetail>()
    val readOnly = route.readOnly

    private val recipeListFlow: Flow<List<Recipe>> =
        if (readOnly) {
            flow {
                emit(emptyList())
            }
        } else {
            getCropsRecipeFlowUseCase(route.keyword)
        }

    val uiState: StateFlow<CropsInfoDetailUiState> =
        recipeListFlow
            .map { recipeList ->
                val cropsInfo = getCropsInfoByKeyUseCase(route.key).getOrThrow()
                CropsInfoDetailUiState.Success(
                    cropsInfo = cropsInfo.toUiModel(),
                    recipeList = recipeList
                )
            }
            .catch {
                CropsInfoDetailUiState.Loading
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CropsInfoDetailUiState.Loading
            )
}