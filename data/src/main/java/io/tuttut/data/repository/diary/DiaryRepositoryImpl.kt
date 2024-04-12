package io.tuttut.data.repository.diary

import androidx.paging.PagingData
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import io.tuttut.data.constant.FireBaseKey
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.toMap
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
import io.tuttut.data.util.asSnapShotFlow
import io.tuttut.data.util.providePager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class DiaryRepositoryImpl @Inject constructor(
    @Named("gardensRef") val gardenRef: CollectionReference
) : DiaryRepository {
    override fun getDiaryList(gardenId: String, cropsId: String): Flow<PagingData<Diary>>
        = providePager(
            pageSize = 8,
            dataType = Diary::class.java,
            query = gardenRef.document(gardenId)
                .collection(FireBaseKey.DIARY)
                .whereEqualTo(FireBaseKey.DIARY_KEY, cropsId)
                .orderBy(FireBaseKey.DIARY_CREATED, Query.Direction.DESCENDING)
        )

    override fun getFourDiaryList(gardenId: String, cropsId: String): Flow<List<Diary>>
        = gardenRef.document(gardenId)
        .collection(FireBaseKey.DIARY)
        .whereEqualTo(FireBaseKey.DIARY_KEY, cropsId)
        .orderBy(FireBaseKey.DIARY_CREATED, Query.Direction.DESCENDING)
        .asFlow(Diary::class.java)
        .take(4)

    override fun getDiaryDetail(gardenId: String, diaryId: String): Flow<Diary>
        = gardenRef.document(gardenId)
            .collection(FireBaseKey.DIARY)
            .document(diaryId)
            .asSnapShotFlow(Diary::class.java)

    override fun addDiary(gardenId: String, diary: Diary): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId)
        val diaryId = gardenRef.document().id
        val diaryRef = ref.collection(FireBaseKey.DIARY).document(diaryId)
        val cropsRef = ref.collection(FireBaseKey.CROPS).document(diary.cropsId)
        Firebase.firestore.runBatch { batch ->
            batch.set(diaryRef, diary.copy(id = diaryId))
            batch.update(cropsRef, FireBaseKey.CROPS_DIARY_COUNT, FieldValue.increment(1))
            if (diary.imgUrlList.isNotEmpty()) {
                batch.update(cropsRef, FireBaseKey.CROPS_MAIN_IMAGE, diary.imgUrlList[0])
            }
        }.await()
        emit(Result.Success(diaryId))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateDiary(gardenId: String, diary: Diary): Flow<Result<String>> = flow {
        emit(Result.Loading)
        gardenRef.document(gardenId)
            .collection(FireBaseKey.DIARY)
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
        val diaryRef = ref.collection(FireBaseKey.DIARY).document(diary.id)
        val cropsRef = ref.collection(FireBaseKey.CROPS).document(diary.cropsId)
        Firebase.firestore.runBatch { batch ->
            batch.delete(diaryRef)
            batch.update(cropsRef, FireBaseKey.CROPS_DIARY_COUNT, FieldValue.increment(-1))
        }.await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}