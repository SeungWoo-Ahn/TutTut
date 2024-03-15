package io.tuttut.data.repository.crops

import androidx.paging.PagingData
import io.tuttut.data.model.dto.Crops
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface CropsRepository {
    fun getGardenCropsList(gardenId: String, isHarvested: Boolean): Flow<PagingData<Crops>>

    fun getCropsDetail(gardenId: String, cropsId: String): Flow<Result<Crops>>

    fun addCrops(gardenId: String, crops: Crops): Flow<Result<String>>

    fun updateCrops(gardenId: String, crops: Crops): Flow<Result<String>>

    fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<Void>>
}