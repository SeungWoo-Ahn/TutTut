package io.tuttut.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.AuthRepositoryImpl
import io.tuttut.data.repository.comment.CommentRepository
import io.tuttut.data.repository.CommentRepositoryImpl
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.CropsRepositoryImpl
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.data.repository.CropsInfoRepositoryImpl
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.DiaryRepositoryImpl
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.GardenRepositoryImpl
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.data.repository.storage.StorageRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun bindAuthRepository(
        authRepository: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    @Singleton
    internal abstract fun bindCommentRepository(
        commentRepository: CommentRepositoryImpl
    ) : CommentRepository

    @Binds
    @Singleton
    internal abstract fun bindCropsRepository(
        cropsRepository: CropsRepositoryImpl
    ) : CropsRepository

    @Binds
    @Singleton
    internal abstract fun bindCropsInfoRepository(
        cropsInfoRepository: CropsInfoRepositoryImpl
    ) : CropsInfoRepository

    @Binds
    @Singleton
    internal abstract fun bindDiaryRepository(
        diaryRepository: DiaryRepositoryImpl
    ) : DiaryRepository

    @Binds
    @Singleton
    internal abstract fun bindGardenRepository(
        gardenRepository: GardenRepositoryImpl
    ) : GardenRepository

    @Binds
    @Singleton
    internal abstract fun bindStorageRepository(
        storageRepository: StorageRepositoryImpl
    ) : StorageRepository
}