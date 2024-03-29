package io.tuttut.presentation.model

import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Diary
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

    private val _observedCrops = MutableStateFlow(Crops())
    val observedCrops: StateFlow<Crops> = _observedCrops
    private val _recipeLink = MutableStateFlow("")
    val recipeLink: StateFlow<String> = _recipeLink

    private val _observedDiary = MutableStateFlow(Diary())
    val observedDiary: StateFlow<Diary> = _observedDiary
    private val _diaryEditMode = MutableStateFlow(false)
    val diaryEditMode: StateFlow<Boolean> = _diaryEditMode

    fun selectCropsInfo(
        cropsInfo: CropsInfo,
        viewMode: Boolean
    ) {
        _selectedCropsInfo.value = cropsInfo
        _viewMode.value = viewMode
    }

    fun selectCropsState(
        crops: Crops,
        editMode: Boolean = false
    ) {
        _selectedCrops.value = crops
        _editMode.value = editMode
    }

    fun observeCrops(crops: Crops) {
        _observedCrops.value = crops
    }

    fun observeDiary(
        diary: Diary,
        editMode: Boolean = false
    ) {
        _observedDiary.value = diary
        _diaryEditMode.value = editMode
    }

    fun setRecipeLink(link: String) {
        _recipeLink.value = link
    }
}