package io.tuttut.data.repository.crops

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.Crops
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

class CropsRepositoryImpl @Inject constructor(
    @Named("gardensRef") val gardenRef: CollectionReference,
) : CropsRepository {
    override fun getDocumentPath(gardenId: String, cropsId: String): DocumentReference
        = gardenRef.document(gardenId).collection(FirebaseKey.CROPS).document(cropsId)

    override fun getGardenCropsList(gardenId: String, isHarvested: Boolean): Flow<List<Crops>>
        = gardenRef.document(gardenId)
            .collection(FirebaseKey.CROPS)
            .whereEqualTo(FirebaseKey.CROPS_HARVESTED, isHarvested)
            .orderBy(FirebaseKey.CROPS_PLANTING_DATE, Query.Direction.DESCENDING)
            .asFlow(Crops::class.java)


    override fun getCropsDetail(gardenId: String, cropsId: String): Flow<Crops>
        = getDocumentPath(gardenId, cropsId).asSnapShotFlow(Crops::class.java)

    override fun wateringCrops(
        gardenId: String,
        cropsId: String,
        today: String
    ): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = getDocumentPath(gardenId, cropsId)
            .update(
                mapOf(FirebaseKey.CROPS_LAST_WATERED to today)
            ).await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun harvestCrops(gardenId: String, cropsId: String, count: Int): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = getDocumentPath(gardenId, cropsId)
            .update(
                mapOf(
                    FirebaseKey.CROPS_HARVESTED to true,
                    FirebaseKey.CROPS_HARVEST_COUNT to count + 1
                )
            ).await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun addCrops(gardenId: String, crops: Crops): Flow<Result<Crops>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId).collection(FirebaseKey.CROPS)
        val id = ref.document().id
        val newCrops = crops.copy(id = id)
        ref.document(id).set(newCrops).await()
        emit(Result.Success(newCrops))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateCrops(gardenId: String, crops: Crops): Flow<Result<String>> = flow {
        emit(Result.Loading)
        getDocumentPath(gardenId, crops.id).update(crops.toMap()).await()
        emit(Result.Success(crops.id))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = getDocumentPath(gardenId, cropsId).delete().await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}