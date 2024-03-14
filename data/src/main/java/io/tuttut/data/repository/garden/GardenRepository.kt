package io.tuttut.data.repository.garden

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.dto.Garden
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface GardenRepository {
    fun getGardenInfo(gardenId: String): Flow<Result<Garden>>

    fun joinGarden(userData: UserData): Flow<Result<DocumentReference>>

    fun updateGardenInfo(gardenId: String, garden: Garden): Flow<Result<DocumentReference>>

    fun deleteGardenInfo(gardenId: String): Flow<Result<DocumentReference>>
}