package io.tuttut.data.repository.auth

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import io.tuttut.data.constant.FireBaseKey
import io.tuttut.data.model.dto.User
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.context.toUser
import io.tuttut.data.model.dto.toMap
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asSnapShotFlow
import io.tuttut.data.util.asSnapShotResultFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    @Named("usersRef") val usersRef: CollectionReference,
    @Named("gardensRef") val gardensRef: CollectionReference
) : AuthRepository {
    override val currentUser: MutableStateFlow<User> = MutableStateFlow(User())

    override fun getUser(userId: String): Flow<User>
        = usersRef.document(userId).asSnapShotFlow(User::class.java) {
            currentUser.value = it
        }

    override fun getUserResult(userId: String): Flow<Result<User>>
        = usersRef.document(userId).asSnapShotResultFlow(User::class.java)

    override fun join(userData: UserData): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val user = userData.toUser()
        val userRef = usersRef.document(user.id).set(user).await()
        currentUser.emit(user)
        emit(Result.Success(userRef))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateUserInfo(user: User): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = usersRef.document(user.id).update(user.toMap()).await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun withdraw(): Flow<Result<DocumentReference>> = flow {
        emit(Result.Loading)
        val currentUser = currentUser.value
        val ref = usersRef.document(currentUser.id)
        val gardenRef = gardensRef.document(currentUser.gardenId)
        Firebase.firestore.runBatch { batch ->
            batch.delete(ref)
            batch.update(gardenRef, FireBaseKey.GARDEN_GROUP_ID, FieldValue.arrayRemove(currentUser.id))
        }
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}