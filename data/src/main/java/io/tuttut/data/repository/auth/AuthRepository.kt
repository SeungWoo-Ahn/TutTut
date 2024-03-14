package io.tuttut.data.repository.auth

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Response
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {
     fun checkUserExist(userId: String): Flow<Result<User>>

     fun join(userData: UserData, gardenName: String): Flow<Result<DocumentReference>>

     fun getUserInfo(): Flow<Result<User>>

     fun updateUserInfo(): Flow<Result<DocumentReference>>

     fun withdraw(): Flow<Result<DocumentReference>>
}