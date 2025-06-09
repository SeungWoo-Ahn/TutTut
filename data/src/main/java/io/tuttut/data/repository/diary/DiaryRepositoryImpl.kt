package io.tuttut.data.repository.diary

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.Diary
import io.tuttut.data.network.model.toMap
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
import io.tuttut.data.util.asSnapShotFlow
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
    override fun getDiaryList(gardenId: String, cropsId: String): Flow<List<Diary>>
            = gardenRef.document(gardenId)
                .collection(FirebaseKey.DIARY)
                .whereEqualTo(FirebaseKey.DIARY_KEY, cropsId)
                .orderBy(FirebaseKey.DIARY_CREATED, Query.Direction.DESCENDING)
                .asFlow(Diary::class.java)

    override fun getDiaryDetail(gardenId: String, diaryId: String): Flow<Diary>
        = gardenRef.document(gardenId)
            .collection(FirebaseKey.DIARY)
            .document(diaryId)
            .asSnapShotFlow(Diary::class.java)

    override fun addDiary(gardenId: String, diary: Diary): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId)
        val diaryId = gardenRef.document().id
        val diaryRef = ref.collection(FirebaseKey.DIARY).document(diaryId)
        val cropsRef = ref.collection(FirebaseKey.CROPS).document(diary.cropsId)
        Firebase.firestore.runBatch { batch ->
            batch.set(diaryRef, diary.copy(id = diaryId))
            batch.update(cropsRef, FirebaseKey.CROPS_DIARY_COUNT, FieldValue.increment(1))
            if (diary.imgUrlList.isNotEmpty()) {
                batch.update(cropsRef, FirebaseKey.CROPS_MAIN_IMAGE, diary.imgUrlList[0])
            }
        }.await()
        emit(Result.Success(diaryId))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateDiary(gardenId: String, diary: Diary): Flow<Result<String>> = flow {
        emit(Result.Loading)
        gardenRef.document(gardenId)
            .collection(FirebaseKey.DIARY)
            .document(diary.id)
            .update(diary.toMap())
            .await()
        emit(Result.Success(diary.id))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteDiary(gardenId: String, diary: Diary): Flow<Result<DocumentReference>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId)
        val diaryRef = ref.collection(FirebaseKey.DIARY).document(diary.id)
        val cropsRef = ref.collection(FirebaseKey.CROPS).document(diary.cropsId)
        Firebase.firestore.runBatch { batch ->
            batch.delete(diaryRef)
            batch.update(cropsRef, FirebaseKey.CROPS_DIARY_COUNT, FieldValue.increment(-1))
        }.await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}