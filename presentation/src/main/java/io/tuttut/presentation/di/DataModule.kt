package io.tuttut.presentation.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.constant.FireStoreKey
import io.tuttut.data.repository.AuthRepository
import io.tuttut.data.repository.AuthRepositoryImpl
import io.tuttut.data.repository.CropsInfoRepository
import io.tuttut.data.repository.CropsInfoRepositoryImpl
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    @Named("usersRef")
    fun provideUsersRef() = Firebase.firestore.collection(FireStoreKey.USERS)

    @Singleton
    @Provides
    @Named("gardensRef")
    fun provideGardensRef() = Firebase.firestore.collection(FireStoreKey.GARDENS)

    @Singleton
    @Provides
    @Named("cropsInfoRef")
    fun provideCropsInfoRef() = Firebase.firestore.collection(FireStoreKey.CROPS_INFO)

    @Singleton
    @Provides
    fun provideAuthRepository(
        @Named("usersRef") usersRef: CollectionReference,
        @Named("gardensRef") gardensRef: CollectionReference
    ): AuthRepository = AuthRepositoryImpl(usersRef, gardensRef)

    @Singleton
    @Provides
    fun provideCropsInfoRepository(
        @Named("cropsInfoRef") cropsInfoRef: CollectionReference
    ): CropsInfoRepository = CropsInfoRepositoryImpl(cropsInfoRef)
}