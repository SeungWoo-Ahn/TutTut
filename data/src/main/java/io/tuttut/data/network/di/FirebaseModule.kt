package io.tuttut.data.network.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tuttut.data.network.constant.FirebaseKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @UsersReference
    @Provides
    @Singleton
    fun providesUsersRef(): CollectionReference = Firebase.firestore.collection(FirebaseKey.USERS)

    @CropsInfoReference
    @Provides
    @Singleton
    fun providesCropsInfoRef(): CollectionReference = Firebase.firestore.collection(FirebaseKey.CROPS_INFO)

    @GardensReference
    @Provides
    @Singleton
    fun providesGardensRef(): CollectionReference = Firebase.firestore.collection(FirebaseKey.GARDENS)

    @DiaryImageReference
    @Provides
    @Singleton
    fun providesDiaryImageRef(): StorageReference = Firebase.storage.getReference(FirebaseKey.DIARY_IMAGE_KEY)

    @ProfileImageReference
    @Provides
    @Singleton
    fun providesProfileImageRef(): StorageReference = Firebase.storage.getReference(FirebaseKey.USER_IMAGE_KEY)
}