package io.tuttut.domain.repository

import io.tuttut.domain.model.diary.AddDiaryRequest
import io.tuttut.domain.model.diary.DeleteDiaryRequest
import io.tuttut.domain.model.diary.Diary
import io.tuttut.domain.model.diary.UpdateDiaryRequest
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun getDiaryListFlow(gardenId: String, cropsId: String): Flow<List<Diary>>

    fun getDiaryFlow(gardenId: String, diaryId: String): Flow<Diary>

    suspend fun addDiary(addDiaryRequest: AddDiaryRequest): String

    suspend fun updateDiary(updateDiaryRequest: UpdateDiaryRequest): String

    suspend fun deleteDiary(deleteDiaryRequest: DeleteDiaryRequest)
}