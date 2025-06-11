package io.tuttut.data.repository

import io.tuttut.data.datasource.LocalDataCache
import io.tuttut.data.datastore.PreferenceDataStore
import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore,
    private val localDataCache: LocalDataCache,
) : PreferenceRepository {
    override fun getCredentialFlow(): Flow<Credential> =
        preferenceDataStore.userIdFlow
            .combine(preferenceDataStore.gardenIdFlow) { userId, gardenId ->
                if (userId == null || gardenId == null) {
                    throw ExceptionBoundary.UnAuthenticated()
                }
                Credential(userId, gardenId)
            }

    override suspend fun setUserId(id: String) {
        preferenceDataStore.setUserId(id)
    }

    override suspend fun setGardenId(id: String) {
        preferenceDataStore.setGardenId(id)
    }

    override suspend fun clearUserData() {
        preferenceDataStore.clearUserData()
        localDataCache.clearData()
    }

    override fun getCurrentUser(): User? {
        return localDataCache.currentUser.value
    }

    override fun setCurrentUser(user: User) {
        localDataCache.setCurrentUser(user)
    }

    override fun updateCurrentUser(updateUserRequest: UpdateUserRequest) {
        localDataCache.updateCurrentUser(updateUserRequest)
    }

    override fun getGardenUserById(id: String): User? {
        return localDataCache.getGardenUserById(id)
    }

    override fun setGardenUser(user: User) {
        localDataCache.setGardenUser(user)
    }

    override fun getCropsInfoList(): List<CropsInfo> {
        return localDataCache.cropsInfoList.value
    }

    override fun setCropsInfoList(cropsInfoList: List<CropsInfo>) {
        localDataCache.setCropsInfoList(cropsInfoList)
    }

    override fun getCropsInfoByKey(cropsKey: CropsKey): CropsInfo? {
        return localDataCache.getCropsInfoByKey(cropsKey)
    }

    override fun setCropsInfo(cropsInfo: CropsInfo) {
        localDataCache.setCropsInfo(cropsInfo)
    }
}