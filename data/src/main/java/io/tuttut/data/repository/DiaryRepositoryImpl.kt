package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.tuttut.data.mapper.toDomain
import io.tuttut.data.mapper.toDto
import io.tuttut.data.mapper.toUpdateMap
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.DiaryDto
import io.tuttut.data.network.di.FireStoreDB
import io.tuttut.data.network.di.GardensReference
import io.tuttut.data.util.asFlow
import io.tuttut.domain.model.diary.AddDiaryRequest
import io.tuttut.domain.model.diary.DeleteDiaryRequest
import io.tuttut.domain.model.diary.Diary
import io.tuttut.domain.model.diary.UpdateDiaryRequest
import io.tuttut.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    @FireStoreDB val db: FirebaseFirestore,
    @GardensReference val gardenRef: CollectionReference
) : DiaryRepository {
    private fun getPath(gardenId: String): CollectionReference =
        gardenRef.document(gardenId).collection(FirebaseKey.DIARY)

    override fun getDiaryListFlow(gardenId: String, cropsId: String): Flow<List<Diary>> =
        getPath(gardenId)
            .whereEqualTo(FirebaseKey.DIARY_KEY, cropsId)
            .orderBy(FirebaseKey.DIARY_CREATED, Query.Direction.DESCENDING)
            .asFlow<DiaryDto>()
            .map { it.map(DiaryDto::toDomain) }

    override fun getDiaryFlow(gardenId: String, diaryId: String): Flow<Diary> =
        getPath(gardenId)
            .document(diaryId)
            .asFlow<DiaryDto>()
            .map(DiaryDto::toDomain)

    override suspend fun addDiary(addDiaryRequest: AddDiaryRequest): String {
        val gardenId = addDiaryRequest.credential.gardenId
        val id = getPath(gardenId).document().id
        val diaryDoc = getPath(gardenId).document(id)
        val cropsDoc = gardenRef.document(gardenId).collection(FirebaseKey.CROPS).document(addDiaryRequest.cropsId)
        db.runBatch { batch ->
            batch.set(diaryDoc, addDiaryRequest.toDto(id))
            batch.update(cropsDoc, addDiaryRequest.toUpdateMap())
        }.await()
        return id
    }

    override suspend fun updateDiary(updateDiaryRequest: UpdateDiaryRequest): String {
        val (id, gardenId) = updateDiaryRequest
        val updateMap = updateDiaryRequest.toUpdateMap()
        getPath(gardenId).document(id).update(updateMap).await()
        return id
    }

    override suspend fun deleteDiary(deleteDiaryRequest: DeleteDiaryRequest) {
        val (id, gardenId, cropsId) = deleteDiaryRequest
        val diaryDoc = getPath(gardenId).document(id)
        val cropsDoc = gardenRef.document(gardenId).collection(FirebaseKey.CROPS).document(cropsId)
        db.runBatch { batch ->
            batch.delete(diaryDoc)
            batch.update(cropsDoc, FirebaseKey.CROPS_DIARY_COUNT, FieldValue.increment(-1))
        }.await()
    }
}