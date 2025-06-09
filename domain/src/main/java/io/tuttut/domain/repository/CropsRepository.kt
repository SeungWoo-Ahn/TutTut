package io.tuttut.domain.repository

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.model.crops.PutCropsRequest
import kotlinx.coroutines.flow.Flow

interface CropsRepository {
    fun getCropsListFlow(gardenId: String, isHarvest: Boolean): Flow<List<Crops>>

    fun getCropsFlow(gardenId: String, cropsId: String): Flow<Crops>

    suspend fun putCrops(gardenId: String, putCropsRequest: PutCropsRequest): String

    suspend fun wateringCrops(gardenId: String, cropsId: String)

    suspend fun harvestCrops(gardenId: String, cropsId: String)

    suspend fun deleteCrops(gardenId: String, cropsId: String)
}