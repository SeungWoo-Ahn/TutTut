package io.tuttut.data.repository.crops

import io.tuttut.data.model.dto.Crops
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface CropsRepository {
    fun getGardenCropsList(gardenId: String, isHarvested: Boolean, lastVisibleItem: MutableStateFlow<Int>): Flow<Result<List<Crops>>>

    fun getCropsDetail(gardenId: String, cropsId: String): Flow<Result<Crops>>

    fun addCrops(gardenId: String, crops: Crops): Flow<Result<String>>

    fun updateCrops(gardenId: String, crops: Crops): Flow<Result<String>>

    fun deleteCrops(gardenId: String, cropsId: String): Flow<Result<Void>>
}