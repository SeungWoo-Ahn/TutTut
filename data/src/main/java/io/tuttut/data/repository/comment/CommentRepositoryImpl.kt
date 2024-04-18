package io.tuttut.data.repository.comment

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import io.tuttut.data.constant.FireBaseKey
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class CommentRepositoryImpl @Inject constructor(
    @Named("gardensRef") val gardensRef: CollectionReference
) : CommentRepository {
    override fun getCollectionPath(gardenId: String, diaryId: String): CollectionReference
        = gardensRef.document(gardenId).collection(FireBaseKey.DIARY).document(diaryId).collection(FireBaseKey.COMMENT)


    override fun getDiaryComments(gardenId: String, diaryId: String): Flow<List<Comment>>
        = getCollectionPath(gardenId, diaryId)
            .orderBy(FireBaseKey.COMMENT_CREATED, Query.Direction.ASCENDING)
            .asFlow(Comment::class.java)


    override fun addDiaryComment(
        gardenId: String,
        diaryId: String,
        comment: Comment
    ): Flow<Result<DocumentReference>> = flow {
        emit(Result.Loading)
        val commentId = gardensRef.document().id
        val ref = getCollectionPath(gardenId, diaryId).document(commentId)
        val diaryRef = gardensRef.document(gardenId).collection(FireBaseKey.DIARY).document(diaryId)
        Firebase.firestore.runBatch { batch ->
            batch.set(ref, comment.copy(id = commentId))
            batch.update(diaryRef, FireBaseKey.DIARY_COMMENT_COUNT, FieldValue.increment(1))
        }.await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteDiaryComment(
        gardenId: String,
        diaryId: String,
        commentId: String
    ): Flow<Result<DocumentReference>> = flow {
        emit(Result.Loading)
        val ref = getCollectionPath(gardenId, diaryId).document(commentId)
        val diaryRef = gardensRef.document(gardenId).collection(FireBaseKey.DIARY).document(diaryId)
        Firebase.firestore.runBatch { batch ->
            batch.delete(ref)
            batch.update(diaryRef, FireBaseKey.DIARY_COMMENT_COUNT, FieldValue.increment(-1))
        }
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteAllDiaryComments(gardenId: String, diaryId: String) {
        val collectionPath = getCollectionPath(gardenId, diaryId)
        val comments = collectionPath.get().await().toObjects(Comment::class.java)
        comments.forEach { comment ->
            collectionPath.document(comment.id).delete().await()
        }
    }
}