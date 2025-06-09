package io.tuttut.data.repository.comment

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.CommentDto
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
        = gardensRef.document(gardenId).collection(FirebaseKey.DIARY).document(diaryId).collection(
        FirebaseKey.COMMENT)


    override fun getDiaryComments(gardenId: String, diaryId: String): Flow<List<CommentDto>>
        = getCollectionPath(gardenId, diaryId)
            .orderBy(FirebaseKey.COMMENT_CREATED, Query.Direction.ASCENDING)
            .asFlow(CommentDto::class.java)


    override fun addDiaryComment(
        gardenId: String,
        diaryId: String,
        comment: CommentDto
    ): Flow<Result<DocumentReference>> = flow {
        emit(Result.Loading)
        val commentId = gardensRef.document().id
        val ref = getCollectionPath(gardenId, diaryId).document(commentId)
        val diaryRef = gardensRef.document(gardenId).collection(FirebaseKey.DIARY).document(diaryId)
        Firebase.firestore.runBatch { batch ->
            batch.set(ref, comment.copy(id = commentId))
            batch.update(diaryRef, FirebaseKey.DIARY_COMMENT_COUNT, FieldValue.increment(1))
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
        val diaryRef = gardensRef.document(gardenId).collection(FirebaseKey.DIARY).document(diaryId)
        Firebase.firestore.runBatch { batch ->
            batch.delete(ref)
            batch.update(diaryRef, FirebaseKey.DIARY_COMMENT_COUNT, FieldValue.increment(-1))
        }
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteAllDiaryComments(gardenId: String, diaryId: String) {
        val collectionPath = getCollectionPath(gardenId, diaryId)
        val comments = collectionPath.get().await().toObjects(CommentDto::class.java)
        comments.forEach { comment ->
            collectionPath.document(comment.id).delete().await()
        }
    }
}