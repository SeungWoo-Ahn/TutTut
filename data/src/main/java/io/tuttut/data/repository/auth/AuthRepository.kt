package io.tuttut.data.repository.auth

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.context.UserData
import io.tuttut.data.network.model.UserDto
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {

     val currentUser: MutableStateFlow<UserDto>

     fun getUser(userId: String): Flow<UserDto>

     fun getUserResult(userId: String): Flow<Result<UserDto>>

     fun join(userData: UserData): Flow<Result<Void>>

     fun updateUserInfo(user: UserDto): Flow<Result<Void>>

     fun withdraw(): Flow<Result<DocumentReference>>
}