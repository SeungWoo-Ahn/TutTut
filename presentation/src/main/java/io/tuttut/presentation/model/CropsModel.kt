package io.tuttut.presentation.model

import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropsModel @Inject constructor() {
    val refreshCropsList = MutableStateFlow(false)
    val refreshHarvestedCropsList = MutableStateFlow(false)

    private val _selectedCropsInfo = MutableStateFlow(CropsInfo())
    val selectedCropsInfo: StateFlow<CropsInfo> = _selectedCropsInfo
    private val _viewMode = MutableStateFlow(false)
    val viewMode: StateFlow<Boolean> = _viewMode

    private val _selectedCrops = MutableStateFlow(Crops())
    val selectedCrops: StateFlow<Crops> = _selectedCrops
    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode

    private val _selectedCropsId = MutableStateFlow("")
    val selectedCropsId: StateFlow<String> = _selectedCropsId

    fun selectCropsInfo(cropsInfo: CropsInfo, viewMode: Boolean) {
        _selectedCropsInfo.value = cropsInfo
        _viewMode.value = viewMode
    }

    fun setCropsState(
        crops: Crops,
        editMode: Boolean = false
    ) {
        _selectedCrops.value = crops
        _editMode.value = editMode
    }

    fun setCropsId(cropsId: String) {
        _selectedCropsId.value = cropsId
    }
}