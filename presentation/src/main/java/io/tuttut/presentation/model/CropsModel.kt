package io.tuttut.presentation.model

import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropsModel @Inject constructor() {
    private val _selectedCropsInfo = MutableStateFlow(CropsInfo())
    val selectedCropsInfo: StateFlow<CropsInfo> = _selectedCropsInfo
    private val _viewMode = MutableStateFlow(false)
    val viewMode: StateFlow<Boolean> = _viewMode

    private val _observedCrops = MutableStateFlow(Crops())
    val observedCrops: StateFlow<Crops> = _observedCrops
    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode
    private val _recipeLink = MutableStateFlow("")
    val recipeLink: StateFlow<String> = _recipeLink

    fun selectCropsInfo(
        cropsInfo: CropsInfo,
        viewMode: Boolean
    ) {
        _selectedCropsInfo.value = cropsInfo
        _viewMode.value = viewMode
    }

    fun observeCrops(crops: Crops, editMode: Boolean = false) {
        _observedCrops.value = crops
        _editMode.value = editMode
    }

    fun setRecipeLink(link: String) {
        _recipeLink.value = link
    }
}