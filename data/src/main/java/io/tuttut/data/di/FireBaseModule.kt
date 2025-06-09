package io.tuttut.data.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.network.constant.FirebaseKey
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {

    @Provides
    @Singleton
    @Named("usersRef")
    fun provideUsersRef() = Firebase.firestore.collection(FirebaseKey.USERS)

    @Provides
    @Singleton
    @Named("cropsInfoRef")
    fun provideCropsInfoRef() = Firebase.firestore.collection(FirebaseKey.CROPS_INFO)

    @Provides
    @Singleton
    @Named("gardensRef")
    fun provideGardensRef() = Firebase.firestore.collection(FirebaseKey.GARDENS)

    @Provides
    @Singleton
    @Named("diaryImageRef")
    fun provideDiaryImageRef() = Firebase.storage.getReference(FirebaseKey.DIARY_IMAGE_KEY)

    @Provides
    @Singleton
    @Named("profileImageRef")
    fun provideProfileImageRef() = Firebase.storage.getReference(FirebaseKey.USER_IMAGE_KEY)
}