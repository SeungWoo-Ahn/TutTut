package io.tuttut.domain.repository

import io.tuttut.domain.model.garden.CreateGardenRequest
import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.model.garden.JoinGardenRequest
import io.tuttut.domain.model.garden.LeaveGardenRequest
import io.tuttut.domain.model.garden.UpdateGardenRequest

interface GardenRepository {
    suspend fun getGarden(id: String): Result<Garden>

    suspend fun getGardenByCode(code: String): Result<Garden>

    suspend fun createGarden(createGardenRequest: CreateGardenRequest): Result<String>

    suspend fun joinGarden(joinGardenRequest: JoinGardenRequest): Result<Unit>

    suspend fun updateGarden(updateGardenRequest: UpdateGardenRequest): Result<Unit>

    suspend fun leaveGarden(leaveGardenRequest: LeaveGardenRequest): Result<Unit>
}