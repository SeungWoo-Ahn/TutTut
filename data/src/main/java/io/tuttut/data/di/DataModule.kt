package io.tuttut.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.auth.AuthRepositoryImpl
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepositoryImpl
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.garden.GardenRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindAuthRepository(
        authRepository: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    internal abstract fun bindCropsRepository(
        cropsRepository: AuthRepositoryImpl
    ) : CropsRepository

    @Binds
    internal abstract fun bindCropsInfoRepository(
        cropsInfoRepository: CropsInfoRepositoryImpl
    ) : CropsInfoRepository

    @Binds
    internal abstract fun binDiaryRepository(
        diaryRepository: CropsInfoRepositoryImpl
    ) : DiaryRepository

    @Binds
    internal abstract fun binGardenRepository(
        gardenRepository: GardenRepositoryImpl
    ) : GardenRepository
}