package io.tuttut.data.repository.comment

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.constant.FireBaseKey
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.toMap
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asResultFlow
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


    override fun getDiaryComments(gardenId: String, diaryId: String): Flow<Result<List<Comment>>>
        = getCollectionPath(gardenId, diaryId).asResultFlow(Comment::class.java)


    override fun addDiaryComment(
        gardenId: String,
        diaryId: String,
        comment: Comment
    ): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val commentId = gardensRef.document().id
        val ref = getCollectionPath(gardenId, diaryId)
            .document(commentId)
            .set(comment.copy(id = commentId))
            .await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateDiaryComment(
        gardenId: String,
        diaryId: String,
        comment: Comment
    ): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = getCollectionPath(gardenId, diaryId)
            .document(comment.id)
            .update(comment.toMap())
            .await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteDiaryComment(
        gardenId: String,
        diaryId: String,
        commentId: String
    ): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = getCollectionPath(gardenId, diaryId)
            .document(commentId)
            .delete()
            .await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}