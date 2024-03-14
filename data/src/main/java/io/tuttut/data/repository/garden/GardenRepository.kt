package io.tuttut.data.repository.garden

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Garden
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface GardenRepository {

    fun checkGardenExist(gardenCode: String): Flow<Result<List<Garden>>>

    fun getGardenInfo(gardenId: String): Flow<Result<Garden>>

    fun updateGardenInfo(gardenId: String, garden: Garden): Flow<Result<DocumentReference>>

    fun deleteGardenInfo(gardenId: String): Flow<Result<DocumentReference>>
}