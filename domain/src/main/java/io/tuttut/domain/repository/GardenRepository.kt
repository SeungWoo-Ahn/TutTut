package io.tuttut.domain.repository

import io.tuttut.domain.model.garden.CreateGardenRequest
import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.model.user.Credential

interface GardenRepository {
    suspend fun getGarden(id: String): Garden

    suspend fun getGardenByCode(code: String): Garden

    suspend fun createGarden(createGardenRequest: CreateGardenRequest): String

    suspend fun joinGarden(credential: Credential)

    suspend fun updateGarden(id: String, name: String)

    suspend fun leaveGarden(credential: Credential)
}