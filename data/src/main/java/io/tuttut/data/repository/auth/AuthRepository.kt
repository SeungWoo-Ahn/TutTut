package io.tuttut.data.repository.auth

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {

     val currentUser: MutableStateFlow<User>
     fun getUserInfo(userId: String): Flow<Result<User>>

     fun join(userData: UserData, gardenName: String): Flow<Result<DocumentReference>>

     fun joinOtherGarden(userData: UserData, garden: Garden): Flow<Result<DocumentReference>>

     fun updateUserInfo(userId: String, user: User): Flow<Result<Void>>

     fun withdraw(): Flow<Result<DocumentReference>>
}