package io.tuttut.data.repository.diary

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.network.model.DiaryDto
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface DiaryRepository {
    fun getDiaryList(gardenId: String, cropsId: String): Flow<List<DiaryDto>>

    fun getDiaryDetail(gardenId: String, diaryId: String): Flow<DiaryDto>

    fun addDiary(gardenId: String, diary: DiaryDto): Flow<Result<String>>

    fun updateDiary(gardenId: String, diary: DiaryDto): Flow<Result<String>>

   fun deleteDiary(gardenId: String, diary: DiaryDto): Flow<Result<DocumentReference>>
}