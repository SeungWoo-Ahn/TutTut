package io.tuttut.presentation.model

import io.tuttut.data.model.dto.CropsInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropsModel @Inject constructor() {
    private val _selectedCropsInfo = MutableStateFlow(CropsInfo())
    val selectedCropsInfo: StateFlow<CropsInfo> = _selectedCropsInfo

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode

    fun setAddCropsState(
        cropsInfo: CropsInfo = CropsInfo(),
        editMode: Boolean = false
    ) {
        _selectedCropsInfo.value = cropsInfo
        _editMode.value = editMode
    }
}