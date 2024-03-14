package io.tuttut.data.repository.diary

import io.tuttut.data.model.dto.Diary
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface DiaryRepository {
    fun getDiaryList(gardenId: String, cropsId: String): Flow<Result<List<Diary>>>

    fun getDiaryDetail(gardenId: String, diaryId: String): Flow<Result<Diary>>

    fun addDiary(gardenId: String, diary: Diary): Flow<Result<String>>

    fun updateDiary(gardenId: String, diaryId: String, diary: Diary): Flow<Result<String>>

   fun deleteDiary(gardenId: String, diaryId: String): Flow<Result<Void>>
}