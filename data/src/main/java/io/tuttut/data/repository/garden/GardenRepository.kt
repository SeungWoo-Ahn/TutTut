package io.tuttut.data.repository.garden

import io.tuttut.data.model.dto.Garden
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface GardenRepository {

    fun checkGardenExist(gardenCode: String): Flow<Result<List<Garden>>>

    fun getGardenInfo(gardenId: String): Flow<Result<Garden>>

    fun updateGardenInfo(garden: Garden): Flow<Result<Void>>

    fun deleteGardenInfo(gardenId: String): Flow<Result<Void>>
}