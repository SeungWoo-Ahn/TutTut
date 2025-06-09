package io.tuttut.domain.repository

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun getCredentialFlow(): Flow<Credential>

    fun setUserId(id: String)

    fun setGardenId(id: String)

    fun getCurrentUser(): User?

    fun setCurrentUser(user: User)

    fun updateCurrentUser(updateUserRequest: UpdateUserRequest)

    fun getGardenUserById(id: String): User?

    fun setGardenUser(user: User)

    fun clearUserData()

    fun getCropsInfoList(): List<CropsInfo>

    fun setCropsInfoList(cropsInfoList: List<CropsInfo>)

    fun getCropsInfoByKey(cropsKey: CropsKey): CropsInfo?

    fun setCropsInfo(cropsInfo: CropsInfo)
}