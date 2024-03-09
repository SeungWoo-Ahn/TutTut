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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideUsersRef() = Firebase.firestore.collection(FireStoreKey.USERS)

    @Provides
    fun provideAuthRepository(
        usersRef: CollectionReference
    ): AuthRepository = AuthRepositoryImpl(usersRef)
}