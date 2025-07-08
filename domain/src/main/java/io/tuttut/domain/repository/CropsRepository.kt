package io.tuttut.domain.repository

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.model.crops.AddCropsRequest
import io.tuttut.domain.model.crops.UpdateCropsRequest
import kotlinx.coroutines.flow.Flow

interface CropsRepository {
    fun getCropsListFlow(gardenId: String, isHarvest: Boolean): Flow<List<Crops>>

    fun getCropsFlow(gardenId: String, cropsId: String): Flow<Crops>

    suspend fun addCrops(gardenId: String, addCropsRequest: AddCropsRequest): String

    suspend fun updateCrops(gardenId: String, updateCropsRequest: UpdateCropsRequest): String

    suspend fun wateringCrops(gardenId: String, cropsId: String)

    suspend fun harvestCrops(gardenId: String, cropsId: String)

    suspend fun deleteCrops(gardenId: String, cropsId: String)
}