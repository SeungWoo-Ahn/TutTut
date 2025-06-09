package io.tuttut.presentation.model

import io.tuttut.data.network.model.Diary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryModel @Inject constructor() {
    private val _observedDiary = MutableStateFlow(Diary())
    val observedDiary: StateFlow<Diary> = _observedDiary
    private val _diaryEditMode = MutableStateFlow(false)
    val diaryEditMode: StateFlow<Boolean> = _diaryEditMode

    fun observeDiary(
        diary: Diary,
        editMode: Boolean = false
    ) {
        _observedDiary.value = diary
        _diaryEditMode.value = editMode
    }
}