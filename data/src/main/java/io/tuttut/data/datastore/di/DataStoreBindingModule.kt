package io.tuttut.data.datastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.datastore.PreferenceDataStore
import io.tuttut.data.datastore.PreferenceDataStoreImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface DataStoreBindingModule {
    @Binds
    fun bindsPreferenceDataStore(preferenceDataStore: PreferenceDataStoreImpl): PreferenceDataStore
}