package io.tuttut.data.repository.comment

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCollectionPath(gardenId: String, diaryId: String): CollectionReference

    fun getDiaryComments(gardenId: String, diaryId: String): Flow<Result<List<Comment>>>

    fun addDiaryComment(gardenId: String, diaryId: String, comment: Comment): Flow<Result<Void>>

    fun updateDiaryComment(gardenId: String, diaryId: String, comment: Comment): Flow<Result<Void>>

    fun deleteDiaryComment(gardenId: String, diaryId: String, commentId: String): Flow<Result<Void>>
}