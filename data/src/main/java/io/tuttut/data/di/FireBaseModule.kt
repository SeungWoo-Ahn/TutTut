package io.tuttut.data.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.constant.FireBaseKey
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {

    @Provides
    @Singleton
    @Named("usersRef")
    fun provideUsersRef() = Firebase.firestore.collection(FireBaseKey.USERS)

    @Provides
    @Singleton
    @Named("cropsInfoRef")
    fun provideCropsInfoRef() = Firebase.firestore.collection(FireBaseKey.CROPS_INFO)

    @Provides
    @Singleton
    @Named("gardensRef")
    fun provideGardensRef() = Firebase.firestore.collection(FireBaseKey.GARDENS)

    @Provides
    @Singleton
    @Named("diaryImageRef")
    fun provideDiaryImageRef() = Firebase.storage.getReference(FireBaseKey.DIARY_IMAGE_KEY)
}