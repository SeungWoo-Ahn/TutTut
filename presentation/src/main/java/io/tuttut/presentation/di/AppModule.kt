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
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Named("usersRef")
    fun provideUsersRef() = Firebase.firestore.collection(FireStoreKey.USERS)

    @Provides
    @Named("gardensRef")
    fun provideGardensRef() = Firebase.firestore.collection(FireStoreKey.GARDENS)

    @Provides
    fun provideAuthRepository(
        @Named("usersRef") usersRef: CollectionReference,
        @Named("gardensRef") gardensRef: CollectionReference
    ): AuthRepository = AuthRepositoryImpl(usersRef, gardensRef)
}