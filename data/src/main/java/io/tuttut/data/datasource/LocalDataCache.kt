package io.tuttut.data.datasource

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User
import kotlinx.coroutines.flow.StateFlow

interface LocalDataCache {
    val currentUser: StateFlow<User?>

    val cropsInfoList: StateFlow<List<CropsInfo>>

    fun setCurrentUser(user: User)

    fun updateCurrentUser(updateUserRequest: UpdateUserRequest)

    fun getGardenUserById(id: String): User?

    fun setGardenUser(user: User)

    fun setCropsInfoList(cropsInfoList: List<CropsInfo>)

    fun getCropsInfoByKey(cropsKey: CropsKey): CropsInfo?

    fun setCropsInfo(cropsInfo: CropsInfo)

    fun clearData()
}