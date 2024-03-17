package io.tuttut.presentation.ui.screen.main.recipeWebView

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import javax.inject.Inject

@HiltViewModel
class RecipeWebViewModel @Inject constructor(
    cropsModel: CropsModel
) : BaseViewModel() {
    val crops = cropsModel.observedCrops
    val link = cropsModel.recipeLink
}