package io.tuttut.presentation.ui.screen.main.cropsInfoDetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.ui.screen.main.cropsDetail.CropsRecipeUiState
import io.tuttut.presentation.util.getToday
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CropsInfoDetailViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
    private val cropsModel: CropsModel,
): BaseViewModel() {
    val cropsInfo = cropsModel.selectedCropsInfo
    val viewMode = cropsModel.viewMode

    val recipeUiState: StateFlow<CropsRecipeUiState>
        = cropsInfoRepo
            .getCropsRecipes(cropsInfo.value.name)
            .map(CropsRecipeUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CropsRecipeUiState.Loading
            )

    fun onRecipe(link: String, moveRecipeWeb: () -> Unit) {
        cropsModel.setRecipeLink(link)
        moveRecipeWeb()
    }

    fun onButton(moveAdd: () -> Unit) {
        cropsInfo.value.let {
            cropsModel.selectCropsState(
                Crops(
                    key = it.key,
                    name = it.name,
                    wateringInterval = it.wateringInterval,
                    growingDay = it.growingDay,
                    plantingDate = getToday()
                )
            )
        }
        moveAdd()
    }
}