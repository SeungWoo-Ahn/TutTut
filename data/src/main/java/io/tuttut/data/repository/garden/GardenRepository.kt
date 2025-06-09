package io.tuttut.data.repository.garden

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.network.model.GardenDto
import io.tuttut.data.network.model.UserDto
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface GardenRepository {
    val currentGarden: MutableStateFlow<GardenDto>
    val gardenMemberInfo: MutableStateFlow<List<UserDto>>
    val gardenMemberMap: HashMap<String, UserDto>

    fun checkGardenExist(gardenCode: String): Flow<Result<List<GardenDto>>>

    fun createGarden(userId: String, gardenName: String, created: String): Flow<Result<String>>

    fun joinGarden(userId: String, gardenId: String): Flow<Result<String>>

    fun getGardenInfo(gardenId: String): Flow<GardenDto>

    suspend fun getGardenMemberInfo(gardenId: String): Flow<Boolean>

    fun updateGardenInfo(garden: GardenDto): Flow<Result<Void>>

    fun quitGarden(userId: String, gardenId: String): Flow<Result<DocumentReference>>
}