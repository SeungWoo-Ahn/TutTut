package io.tuttut.domain.repository

import io.tuttut.domain.model.comment.AddCommentRequest
import io.tuttut.domain.model.comment.Comment
import io.tuttut.domain.model.comment.DeleteCommentRequest
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCommentListFlow(gardenId: String, diaryId: String): Flow<List<Comment>>

    suspend fun addComment(addCommentRequest: AddCommentRequest)

    suspend fun deleteComment(deleteCommentRequest: DeleteCommentRequest)
}