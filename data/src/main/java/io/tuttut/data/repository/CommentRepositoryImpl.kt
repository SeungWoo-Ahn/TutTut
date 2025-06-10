package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.tuttut.data.mapper.toDomain
import io.tuttut.data.mapper.toDto
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.di.FireStoreDB
import io.tuttut.data.network.di.GardensReference
import io.tuttut.data.network.model.CommentDto
import io.tuttut.data.util.asFlow
import io.tuttut.domain.model.comment.AddCommentRequest
import io.tuttut.domain.model.comment.Comment
import io.tuttut.domain.model.comment.DeleteCommentRequest
import io.tuttut.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    @FireStoreDB val db: FirebaseFirestore,
    @GardensReference val gardensRef: CollectionReference
) : CommentRepository {
    private fun getPath(gardenId: String, diaryId: String): CollectionReference =
        gardensRef
            .document(gardenId)
            .collection(FirebaseKey.DIARY)
            .document(diaryId)
            .collection(FirebaseKey.COMMENT)

    override fun getCommentListFlow(gardenId: String, diaryId: String): Flow<List<Comment>> =
        getPath(gardenId, diaryId)
            .orderBy(FirebaseKey.COMMENT_CREATED, Query.Direction.ASCENDING)
            .asFlow<CommentDto>()
            .map { it.map(CommentDto::toDomain) }

    override suspend fun addComment(addCommentRequest: AddCommentRequest) {
        val (credential, diaryId) = addCommentRequest
        val id = getPath(credential.gardenId, diaryId).document().id
        val commentDoc = getPath(credential.gardenId, diaryId).document(id)
        val diaryDoc = gardensRef.document(credential.gardenId).collection(FirebaseKey.DIARY).document(diaryId)
        db.runBatch { batch ->
            batch.set(commentDoc, addCommentRequest.toDto(id))
            batch.update(diaryDoc, FirebaseKey.DIARY_COMMENT_COUNT, FieldValue.increment(1))
        }.await()
    }

    override suspend fun deleteComment(deleteCommentRequest: DeleteCommentRequest) {
        val (gardenId, diaryId, commentId) = deleteCommentRequest
        val commentDoc = getPath(gardenId, diaryId).document(commentId)
        val diaryDoc = gardensRef.document(gardenId).collection(FirebaseKey.DIARY).document(diaryId)
        db.runBatch { batch ->
            batch.delete(commentDoc)
            batch.update(diaryDoc, FirebaseKey.DIARY_COMMENT_COUNT, FieldValue.increment(-1))
        }.await()
    }
}