package io.tuttut.data.repository.garden

import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface GardenRepository {
    val gardenMemberInfo: MutableStateFlow<List<User>>
    val gardenMemberMap: HashMap<String, User>

    fun checkGardenExist(gardenCode: String): Flow<Result<List<Garden>>>

    fun getGardenInfo(gardenId: String): Flow<Result<Garden>>

    fun getGardenMemberInfo(gardenId: String): Flow<Result<Boolean>>

    fun updateGardenInfo(garden: Garden): Flow<Result<Void>>

    fun deleteGardenInfo(gardenId: String): Flow<Result<Void>>
}