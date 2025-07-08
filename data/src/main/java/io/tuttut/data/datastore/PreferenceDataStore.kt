package io.tuttut.data.datastore

import kotlinx.coroutines.flow.Flow

interface PreferenceDataStore {
    val userIdFlow: Flow<String?>

    val gardenIdFlow: Flow<String?>

    suspend fun setUserId(id: String)

    suspend fun setGardenId(id: String)

    suspend fun clearUserData()
}