package io.tuttut.data.repository.diary

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.Diary
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun getCropsDiaryList(gardenId: String, cropsId: String): Flow<Result<List<Diary>>>

    fun addCropsDiary(gardenId: String, cropsId: String): Flow<Result<DocumentReference>>

    fun updateCropsDiary(gardenId: String, cropsId: String, diaryId: String): Flow<Result<DocumentReference>>

   fun deleteCropsDiary(gardenId: String, cropsId: String, diaryId: String): Flow<Result<DocumentReference>>

   fun getDiaryComments(gardenId: String, cropsId: String, diaryId: String): Flow<Result<List<Comment>>>

   fun addDiaryComment(gardenId: String, cropsId: String, diaryId: String, comment: Comment): Flow<Result<DocumentReference>>

   fun updateDiaryComment(gardenId: String, cropsId: String, diaryId: String, comment: Comment): Flow<Result<DocumentReference>>

   fun deleteDiaryComment(gardenId: String, cropsId: String, diaryId: String, commentId: String): Flow<Result<DocumentReference>>
}