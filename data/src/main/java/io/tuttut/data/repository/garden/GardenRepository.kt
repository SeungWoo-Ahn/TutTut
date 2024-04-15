package io.tuttut.data.repository.garden

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface GardenRepository {
    val currentGarden: MutableStateFlow<Garden>
    val gardenMemberInfo: MutableStateFlow<List<User>>
    val gardenMemberMap: HashMap<String, User>

    fun checkGardenExist(gardenCode: String): Flow<Result<List<Garden>>>

    fun getGardenInfo(gardenId: String): Flow<Garden>

    suspend fun getGardenMemberInfo(gardenId: String): Flow<Boolean>

    fun updateGardenInfo(garden: Garden): Flow<Result<Void>>

    fun quitGarden(userId: String, gardenId: String): Flow<Result<DocumentReference>>
}