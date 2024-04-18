package io.tuttut.data.repository.crops

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Crops
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface CropsRepository {

    fun getDocumentPath(gardenId: String, cropsId: String): DocumentReference

    fun getGardenCropsList(gardenId: String, isHarvested: Boolean): Flow<List<Crops>>

    fun getCropsDetail(gardenId: String, cropsId: String): Flow<Crops>

    fun wateringCrops(gardenId: String, cropsId: String, today: String): Flow<Result<Void>>

    fun harvestCrops(gardenId: String, cropsId: String, count: Int): Flow<Result<Void>>

    fun addCrops(gardenId: String, crops: Crops): Flow<Result<Crops>>

    fun updateCrops(gardenId: String, crops: Crops): Flow<Result<String>>

    fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<Void>>
}