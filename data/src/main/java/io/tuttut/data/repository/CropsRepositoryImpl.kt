package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import io.tuttut.data.mapper.toDomain
import io.tuttut.data.mapper.toDto
import io.tuttut.data.mapper.toUpdateMap
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.CropsDto
import io.tuttut.data.network.di.GardensReference
import io.tuttut.data.util.DateProvider
import io.tuttut.data.util.asFlow
import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.model.crops.AddCropsRequest
import io.tuttut.domain.model.crops.UpdateCropsRequest
import io.tuttut.domain.repository.CropsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropsRepositoryImpl @Inject constructor(
    @GardensReference val gardenRef: CollectionReference,
) : CropsRepository {
    private fun getPath(gardenId: String): CollectionReference
        = gardenRef.document(gardenId).collection(FirebaseKey.CROPS)

    override fun getCropsListFlow(gardenId: String, isHarvest: Boolean): Flow<List<Crops>> =
        getPath(gardenId)
            .whereEqualTo(FirebaseKey.CROPS_HARVESTED, isHarvest)
            .orderBy(FirebaseKey.CROPS_PLANTING_DATE, Query.Direction.DESCENDING)
            .asFlow<CropsDto>()
            .map { it.map(CropsDto::toDomain) }

    override fun getCropsFlow(gardenId: String, cropsId: String): Flow<Crops> =
        getPath(gardenId)
            .document(cropsId)
            .asFlow<CropsDto>()
            .map(CropsDto::toDomain)

    override suspend fun addCrops(gardenId: String, addCropsRequest: AddCropsRequest): String {
        val id = getPath(gardenId).document().id
        val cropsDto = addCropsRequest.toDto(id)
        getPath(gardenId).document(id).set(cropsDto).await()
        return id
    }

    override suspend fun updateCrops(gardenId: String, updateCropsRequest: UpdateCropsRequest): String {
        val id = updateCropsRequest.id
        val updateMap = updateCropsRequest.toUpdateMap()
        getPath(gardenId).document(id).update(updateMap).await()
        return id
    }

    override suspend fun wateringCrops(gardenId: String, cropsId: String) {
        getPath(gardenId)
            .document(cropsId)
            .update(
                mapOf(FirebaseKey.CROPS_LAST_WATERED to DateProvider.now())
            )
            .await()
    }

    override suspend fun harvestCrops(gardenId: String, cropsId: String) {
        getPath(gardenId)
            .document(cropsId)
            .update(
                mapOf(
                    FirebaseKey.CROPS_HARVESTED to true,
                    FirebaseKey.CROPS_HARVEST_COUNT to FieldValue.increment(1)
                )
            )
            .await()
    }

    override suspend fun deleteCrops(gardenId: String, cropsId: String) {
        getPath(gardenId).document(cropsId).delete().await()
    }
}