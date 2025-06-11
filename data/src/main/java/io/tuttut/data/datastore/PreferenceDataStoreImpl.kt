package io.tuttut.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceDataStore {
    override val userIdFlow: Flow<String?>
        get() = dataStore.data.map { store -> store[KEY_USER_ID] }

    override val gardenIdFlow: Flow<String?>
        get() = dataStore.data.map { store -> store[KEY_GARDEN_ID] }

    override suspend fun setUserId(id: String) {
        dataStore.edit { store -> store[KEY_USER_ID] = id }
    }

    override suspend fun setGardenId(id: String) {
        dataStore.edit { store -> store[KEY_GARDEN_ID] = id }
    }

    override suspend fun clearUserData() {
        dataStore.edit { store ->
            store.remove(KEY_USER_ID)
            store.remove(KEY_GARDEN_ID)
        }
    }

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user-id")
        private val KEY_GARDEN_ID = stringPreferencesKey("garden-id")
    }
}