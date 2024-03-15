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

    private val _selectedCrops = MutableStateFlow(Crops())
    val selectedCrops: StateFlow<Crops> = _selectedCrops

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode

    fun selectCropsInfo(cropsInfo: CropsInfo) {
        _selectedCropsInfo.value = cropsInfo
    }

    fun setCropsState(
        crops: Crops,
        editMode: Boolean = false
    ) {
        _selectedCrops.value = crops
        _editMode.value = editMode
    }
}