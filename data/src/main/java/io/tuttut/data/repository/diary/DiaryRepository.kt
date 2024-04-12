package io.tuttut.data.repository.diary

import androidx.paging.PagingData
import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Diary
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface DiaryRepository {
    fun getDiaryList(gardenId: String, cropsId: String): Flow<PagingData<Diary>>

    fun getFourDiaryList(gardenId: String, cropsId: String): Flow<List<Diary>>

    fun getDiaryDetail(gardenId: String, diaryId: String): Flow<Diary>

    fun addDiary(gardenId: String, diary: Diary): Flow<Result<String>>

    fun updateDiary(gardenId: String, diary: Diary): Flow<Result<String>>

   fun deleteDiary(gardenId: String, diary: Diary): Flow<Result<DocumentReference>>
}