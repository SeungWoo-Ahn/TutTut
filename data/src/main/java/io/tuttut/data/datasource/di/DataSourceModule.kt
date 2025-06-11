package io.tuttut.data.datasource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.datasource.LocalDataCache
import io.tuttut.data.datasource.LocalDataCacheImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {
    @Binds
    fun bindsLocalDataCache(localDataCache: LocalDataCacheImpl): LocalDataCache
}