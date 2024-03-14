package io.tuttut.data.repository.crops

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CropsRepositoryImpl @Inject constructor() : CropsRepository {
    override fun getGardenCropsList(
        gardenId: String,
        isHarvested: Boolean
    ): Flow<Result<List<Crops>>> {
        TODO("Not yet implemented")
    }

    override fun getCropsDetail(gardenId: String, cropsId: String): Flow<Result<Crops>> {
        TODO("Not yet implemented")
    }

    override fun addCrops(gardenId: String, crops: Crops): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun updateCrops(gardenId: String, crops: Crops): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

}