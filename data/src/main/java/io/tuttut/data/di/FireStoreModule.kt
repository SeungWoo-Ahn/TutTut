package io.tuttut.data.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.constant.FireStoreKey
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FireStoreModule {

    @Provides
    @Singleton
    @Named("usersRef")
    fun provideUsersRef() = Firebase.firestore.collection(FireStoreKey.USERS)

    @Provides
    @Singleton
    @Named("cropsInfoRef")
    fun provideCropsInfoRef() = Firebase.firestore.collection(FireStoreKey.CROPS_INFO)

    @Provides
    @Singleton
    @Named("gardensRef")
    fun provideGardensRef() = Firebase.firestore.collection(FireStoreKey.GARDENS)
}