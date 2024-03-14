package io.tuttut.data.repository.garden

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.constant.FireStoreKey
import io.tuttut.data.model.dto.Garden
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
import io.tuttut.data.util.asSnapShotFlow
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
        gardenId: String,
        garden: Garden
    ): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun deleteGardenInfo(gardenId: String): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }
}