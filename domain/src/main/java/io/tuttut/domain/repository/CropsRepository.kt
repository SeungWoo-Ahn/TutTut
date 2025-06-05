package io.tuttut.domain.repository

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.model.crops.HarvestCropsRequest
import io.tuttut.domain.model.crops.PutCropsRequest
import io.tuttut.domain.model.crops.WateringCropsRequest
import kotlinx.coroutines.flow.Flow

interface CropsRepository {
    fun getCropsList(gardenId: String, isHarvest: Boolean): Flow<List<Crops>>

    fun getCrops(gardenId: String, cropsId: String): Flow<Crops>

    suspend fun putCrops(gardenId: String, putCropsRequest: PutCropsRequest): Result<String>

    suspend fun wateringCrops(wateringCropsRequest: WateringCropsRequest): Result<Unit>

    suspend fun harvestCrops(harvestCropsRequest: HarvestCropsRequest): Result<Unit>

    suspend fun deleteCrops(gardenId: String, cropsId: String): Result<Unit>
}