package io.tuttut.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.auth.AuthRepositoryImpl
import io.tuttut.data.repository.comment.CommentRepository
import io.tuttut.data.repository.comment.CommentRepositoryImpl
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.crops.CropsRepositoryImpl
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepositoryImpl
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.diary.DiaryRepositoryImpl
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.garden.GardenRepositoryImpl
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
    internal abstract fun binDiaryRepository(
        diaryRepository: DiaryRepositoryImpl
    ) : DiaryRepository

    @Binds
    @Singleton
    internal abstract fun binGardenRepository(
        gardenRepository: GardenRepositoryImpl
    ) : GardenRepository
}