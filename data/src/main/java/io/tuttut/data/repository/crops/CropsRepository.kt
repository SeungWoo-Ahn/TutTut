package io.tuttut.data.repository.crops

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Crops
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface CropsRepository {
    fun getGardenCropsList(gardenId: String, isHarvested: Boolean): Flow<Result<List<Crops>>>

    fun getCropsDetail(gardenId: String, cropsId: String): Flow<Result<Crops>>

    fun addCrops(gardenId: String, crops: Crops): Flow<Result<DocumentReference>>

    fun updateCrops(gardenId: String, crops: Crops): Flow<Result<DocumentReference>>

    fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<DocumentReference>>
}