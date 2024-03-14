package io.tuttut.data.repository.garden

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.constant.FireStoreKey
import io.tuttut.data.model.dto.Garden
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
import io.tuttut.data.util.asSnapShotFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class GardenRepositoryImpl @Inject constructor(
    @Named("gardensRef") val gardensRef: CollectionReference
) : GardenRepository {
    override fun checkGardenExist(gardenCode: String): Flow<Result<List<Garden>>>
        = gardensRef.whereEqualTo(FireStoreKey.GARDEN_CODE, gardenCode).asFlow(Garden::class.java)

    override fun getGardenInfo(gardenId: String): Flow<Result<Garden>>
        = gardensRef.document(gardenId).asSnapShotFlow(Garden::class.java)

    override fun updateGardenInfo(
        garden: Garden
    ): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = gardensRef.document(garden.id).update(
            mapOf(
                "name" to garden.name
            )
        ).await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteGardenInfo(gardenId: String): Flow<Result<Void>> {
        TODO("Not yet implemented")
    }
}