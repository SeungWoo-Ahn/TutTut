package io.tuttut.data.repository.auth

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.dto.User
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {

     val currentUser: MutableStateFlow<User>

     fun getUser(userId: String): Flow<User>

     fun getUserResult(userId: String): Flow<Result<User>>

     fun join(userData: UserData): Flow<Result<Void>>

     fun updateUserInfo(user: User): Flow<Result<Void>>

     fun withdraw(): Flow<Result<DocumentReference>>
}