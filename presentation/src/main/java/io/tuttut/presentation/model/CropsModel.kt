package io.tuttut.presentation.model

import io.tuttut.data.network.model.CropsDto
import io.tuttut.data.network.model.CropsInfoDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropsModel @Inject constructor() {
    private val _selectedCropsInfo = MutableStateFlow(CropsInfoDto())
    val selectedCropsInfo: StateFlow<CropsInfoDto> = _selectedCropsInfo
    private val _viewMode = MutableStateFlow(false)
    val viewMode: StateFlow<Boolean> = _viewMode

    private val _selectedCrops = MutableStateFlow(CropsDto())
    val selectedCrops: StateFlow<CropsDto> = _selectedCrops
    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode

    private val _observedCrops = MutableStateFlow(CropsDto())
    val observedCrops: StateFlow<CropsDto> = _observedCrops
    private val _recipeLink = MutableStateFlow("")
    val recipeLink: StateFlow<String> = _recipeLink

    fun selectCropsInfo(
        cropsInfo: CropsInfoDto,
        viewMode: Boolean
    ) {
        _selectedCropsInfo.value = cropsInfo
        _viewMode.value = viewMode
    }

    fun selectCropsState(
        crops: CropsDto,
        editMode: Boolean = false
    ) {
        _selectedCrops.value = crops
        _editMode.value = editMode
    }

    fun observeCrops(crops: CropsDto) {
        _observedCrops.value = crops
    }



    fun setRecipeLink(link: String) {
        _recipeLink.value = link
    }
}