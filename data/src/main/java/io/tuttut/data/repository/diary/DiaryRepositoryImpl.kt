package io.tuttut.data.repository.diary

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import io.tuttut.data.constant.FireStoreKey
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.toMap
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
import io.tuttut.data.util.asSnapShotResultFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class DiaryRepositoryImpl @Inject constructor(
    @Named("gardensRef") val gardenRef: CollectionReference
) : DiaryRepository {
    override fun getDiaryList(gardenId: String, cropsId: String): Flow<Result<List<Diary>>>
        = gardenRef.document(gardenId)
            .collection(FireStoreKey.DIARY)
            .whereEqualTo(FireStoreKey.DIARY_KEY, cropsId)
            .asFlow(Diary::class.java)

    override fun getDiaryDetail(gardenId: String, diaryId: String): Flow<Result<Diary>>
        = gardenRef.document(gardenId)
            .collection(FireStoreKey.DIARY)
            .document(diaryId)
            .asSnapShotResultFlow(Diary::class.java)

    override fun addDiary(gardenId: String, diary: Diary): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId)
        val diaryId = ref.id
        val diaryRef = ref.collection(FireStoreKey.DIARY).document(diaryId)
        val cropsRef = ref.collection(FireStoreKey.CROPS).document(diary.cropsId)
        Firebase.firestore.runBatch { batch ->
            batch.set(diaryRef, diary.copy(id = diaryId))
            cropsRef.update(FireStoreKey.CROPS_DIARY_CNT, FieldValue.increment(1))
        }.await()
        emit(Result.Success(diaryId))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateDiary(gardenId: String, diaryId: String, diary: Diary): Flow<Result<String>> = flow {
        emit(Result.Loading)
        gardenRef.document(gardenId)
            .collection(FireStoreKey.DIARY)
            .document(diaryId)
            .update(diary.toMap())
            .await()
        emit(Result.Success(diaryId))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteDiary(gardenId: String, diary: Diary): Flow<Result<DocumentReference>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId)
        val diaryRef = ref.collection(FireStoreKey.DIARY).document(diary.id)
        val cropsRef = ref.collection(FireStoreKey.CROPS).document(diary.cropsId)
        Firebase.firestore.runBatch { batch ->
            batch.delete(diaryRef)
            cropsRef.update(FireStoreKey.CROPS_DIARY_CNT, FieldValue.increment(-1))
        }.await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}