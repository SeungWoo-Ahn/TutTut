package io.tuttut.data.repository.comment

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.network.model.CommentDto
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCollectionPath(gardenId: String, diaryId: String): CollectionReference

    fun getDiaryComments(gardenId: String, diaryId: String): Flow<List<CommentDto>>

    fun addDiaryComment(gardenId: String, diaryId: String, comment: CommentDto): Flow<Result<DocumentReference>>

    fun deleteDiaryComment(gardenId: String, diaryId: String, commentId: String): Flow<Result<DocumentReference>>

    suspend fun deleteAllDiaryComments(gardenId: String, diaryId: String)
}