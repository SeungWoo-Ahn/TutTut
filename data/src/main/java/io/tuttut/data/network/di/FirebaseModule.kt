package io.tuttut.data.network.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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
    @Provides
    @Singleton
    fun providesFireStore(): FirebaseFirestore = Firebase.firestore

    @UsersReference
    @Provides
    @Singleton
    fun providesUsersRef(fireStore: FirebaseFirestore): CollectionReference =
        fireStore.collection(FirebaseKey.USERS)

    @CropsInfoReference
    @Provides
    @Singleton
    fun providesCropsInfoRef(fireStore: FirebaseFirestore): CollectionReference =
        fireStore.collection(FirebaseKey.CROPS_INFO)

    @GardensReference
    @Provides
    @Singleton
    fun providesGardensRef(fireStore: FirebaseFirestore): CollectionReference =
        fireStore.collection(FirebaseKey.GARDENS)

    @Provides
    @Singleton
    fun providesStorage(): FirebaseStorage = Firebase.storage

    @DiaryImageReference
    @Provides
    @Singleton
    fun providesDiaryImageRef(storage: FirebaseStorage): StorageReference =
        storage.getReference(FirebaseKey.DIARY_IMAGE_KEY)

    @ProfileImageReference
    @Provides
    @Singleton
    fun providesProfileImageRef(storage: FirebaseStorage): StorageReference =
        storage.getReference(FirebaseKey.USER_IMAGE_KEY)
}