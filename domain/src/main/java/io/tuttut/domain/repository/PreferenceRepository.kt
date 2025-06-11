package io.tuttut.domain.repository

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun getCredentialFlow(): Flow<Credential>

    suspend fun setUserId(id: String)

    suspend fun setGardenId(id: String)

    suspend fun clearUserData()

    fun getCurrentUser(): User?

    fun setCurrentUser(user: User)

    fun updateCurrentUser(updateUserRequest: UpdateUserRequest)

    fun getGardenUserById(id: String): User?

    fun setGardenUser(user: User)

    fun getCropsInfoList(): List<CropsInfo>

    fun setCropsInfoList(cropsInfoList: List<CropsInfo>)

    fun getCropsInfoByKey(cropsKey: CropsKey): CropsInfo?

    fun setCropsInfo(cropsInfo: CropsInfo)
}