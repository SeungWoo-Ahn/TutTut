package io.tuttut.data.datasource

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataCacheImpl @Inject constructor() : LocalDataCache  {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> get() = _currentUser

    private val _cropsInfoList = MutableStateFlow<List<CropsInfo>>(emptyList())
    override val cropsInfoList: StateFlow<List<CropsInfo>> get() = _cropsInfoList

    private val gardenUserMap = ConcurrentHashMap<String, User>()
    private val cropsInfoMap = ConcurrentHashMap<CropsKey, CropsInfo>()

    override fun setCurrentUser(user: User) {
        _currentUser.update { user }
    }

    override fun updateCurrentUser(updateUserRequest: UpdateUserRequest) {
        _currentUser.update {
            _currentUser.value?.copy(
                name = updateUserRequest.name,
                profile = updateUserRequest.profile
            )
        }
    }

    override fun getGardenUserById(id: String): User? {
        return gardenUserMap[id]
    }

    override fun setGardenUser(user: User) {
        gardenUserMap[user.id] = user
    }

    override fun setCropsInfoList(cropsInfoList: List<CropsInfo>) {
        _cropsInfoList.update { cropsInfoList }
        cropsInfoList.forEach {
            cropsInfoMap[it.key] = it
        }
    }

    override fun getCropsInfoByKey(cropsKey: CropsKey): CropsInfo? {
        return cropsInfoMap[cropsKey]
    }

    override fun setCropsInfo(cropsInfo: CropsInfo) {
        cropsInfoMap[cropsInfo.key] = cropsInfo
    }

    override fun clearData() {
        _currentUser.update { null }
        _cropsInfoList.update { emptyList() }
        gardenUserMap.clear()
    }
}