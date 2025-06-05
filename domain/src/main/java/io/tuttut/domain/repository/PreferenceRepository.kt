package io.tuttut.domain.repository

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun getUserIdFlow(): Flow<String>

    fun setUserId(id: String)

    fun getGardenIdFlow(): Flow<String>

    fun setGardenId(id: String)

    fun getCurrentUser(): User

    fun setCurrentUser(user: User)

    fun getGardenUserById(id: String): User?

    fun setGardenUserList(users: List<User>)

    fun getCropsInfoList(): List<CropsInfo>

    fun setCropsInfoList(cropsInfoList: List<CropsInfo>)

    fun getCropsInfoByKey(cropsKey: CropsKey): CropsInfo
}