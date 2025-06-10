package io.tuttut.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.repository.AuthRepositoryImpl
import io.tuttut.data.repository.CommentRepositoryImpl
import io.tuttut.data.repository.CropsInfoRepositoryImpl
import io.tuttut.data.repository.CropsRepositoryImpl
import io.tuttut.data.repository.DiaryRepositoryImpl
import io.tuttut.data.repository.GardenRepositoryImpl
import io.tuttut.data.repository.ImageRepositoryImpl
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.CommentRepository
import io.tuttut.domain.repository.CropsInfoRepository
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.ImageRepository

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindsAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindsGardenRepository(gardenRepository: GardenRepositoryImpl): GardenRepository

    @Binds
    fun bindsCropsRepository(cropsRepository: CropsRepositoryImpl): CropsRepository

    @Binds
    fun bindsCropsInfoRepository(cropsInfoRepository: CropsInfoRepositoryImpl): CropsInfoRepository

    @Binds
    fun bindsDiaryRepository(diaryRepository: DiaryRepositoryImpl): DiaryRepository

    @Binds
    fun bindsCommentRepository(commentRepository: CommentRepositoryImpl): CommentRepository

    @Binds
    fun bindsImageRepository(imageRepository: ImageRepositoryImpl): ImageRepository
}