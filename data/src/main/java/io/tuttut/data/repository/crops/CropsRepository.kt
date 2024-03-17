package io.tuttut.data.repository.crops

import androidx.paging.PagingData
import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Crops
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface CropsRepository {

    fun getDocumentPath(gardenId: String, cropsId: String): DocumentReference

    fun getGardenCropsList(gardenId: String, isHarvested: Boolean): Flow<PagingData<Crops>>

    fun getCropsDetail(gardenId: String, cropsId: String): Flow<Crops>

    fun wateringCrops(gardenId: String, cropsId: String, today: String): Flow<Result<Void>>

    fun addCrops(gardenId: String, crops: Crops): Flow<Result<String>>

    fun updateCrops(gardenId: String, crops: Crops): Flow<Result<String>>

    fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<Void>>
}