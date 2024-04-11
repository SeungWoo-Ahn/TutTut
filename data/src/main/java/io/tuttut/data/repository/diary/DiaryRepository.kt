package io.tuttut.data.repository.diary

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Diary
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface DiaryRepository {
    fun getDiaryList(gardenId: String, cropsId: String): Flow<List<Diary>>

    fun getFourDiaryList(gardenId: String, cropsId: String): Flow<List<Diary>>

    fun getDiaryDetail(gardenId: String, diaryId: String): Flow<Result<Diary>>

    fun addDiary(gardenId: String, diary: Diary): Flow<Result<String>>

    fun updateDiary(gardenId: String, diaryId: String, diary: Diary): Flow<Result<String>>

   fun deleteDiary(gardenId: String, diary: Diary): Flow<Result<DocumentReference>>
}