package io.tuttut.data.repository.crops

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.constant.FireStoreKey
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.toMap
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asSnapShotFlow
import io.tuttut.data.util.paginate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class CropsRepositoryImpl @Inject constructor(
    @Named("gardensRef") val gardenRef: CollectionReference,
) : CropsRepository {
    override fun getGardenCropsList(
        gardenId: String,
        isHarvested: Boolean,
        lastVisibleItem: MutableStateFlow<Int>,
    ): Flow<Result<List<Crops>>>
        = gardenRef.document(gardenId)
            .collection(FireStoreKey.CROPS)
            .whereEqualTo(FireStoreKey.CROPS_HARVESTED, isHarvested)
            .paginate(Crops::class.java, 10, lastVisibleItem)

    override fun getCropsDetail(gardenId: String, cropsId: String): Flow<Result<Crops>>
        = gardenRef.document(gardenId)
            .collection(FireStoreKey.CROPS)
            .document(cropsId)
            .asSnapShotFlow(Crops::class.java)

    override fun addCrops(gardenId: String, crops: Crops): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId).collection(FireStoreKey.CROPS)
        val id = ref.document().id
        ref.document(id).set(crops.copy(id = id)).await()
        emit(Result.Success(id))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateCrops(gardenId: String, crops: Crops): Flow<Result<String>> = flow {
        emit(Result.Loading)
        gardenRef.document(gardenId)
            .collection(FireStoreKey.CROPS)
            .document(crops.id)
            .update(crops.toMap())
            .await()
        emit(Result.Success(crops.id))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = gardenRef.document(gardenId)
            .collection(FireStoreKey.CROPS)
            .document(cropsId)
            .delete()
            .await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}