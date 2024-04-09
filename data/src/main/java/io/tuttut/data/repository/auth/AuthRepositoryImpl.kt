package io.tuttut.data.repository.auth

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import io.tuttut.data.constant.FireBaseKey
import io.tuttut.data.model.dto.User
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asSnapShotResultFlow
import io.tuttut.data.util.getDate
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

    override fun getUserInfo(userId: String): Flow<Result<User>> = usersRef.document(userId).asSnapShotResultFlow(User::class.java) {
        currentUser.value = it
    }

    override fun join(userData: UserData, gardenName: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val gardenId = usersRef.document().id
        val user = User(
            id = userData.userId,
            gardenId = gardenId,
            name = userData.userName!!,
            profileUrl = userData.profileUrl
        )
        val garden = Garden(
            id = gardenId,
            code = gardenId.substring(0, 6),
            name = gardenName,
            created = getDate(),
            groupIdList = listOf(userData.userId),
        )
        val userRef = usersRef.document(userData.userId)
        val gardenRef = gardensRef.document(gardenId)
        Firebase.firestore.runBatch { batch ->
            batch.set(userRef, user)
            batch.set(gardenRef, garden)
        }.await()
        currentUser.emit(user)
        emit(Result.Success(gardenId))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun joinOtherGarden(
        userData: UserData,
        garden: Garden
    ): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val user = User(
            id = userData.userId,
            name = userData.userName!!,
            profileUrl = userData.profileUrl,
            gardenId = garden.id,
        )
        val userRef = usersRef.document(user.id)
        val gardenRef = gardensRef.document(garden.id)
        Firebase.firestore.runBatch { batch ->
            batch.set(userRef, user)
            batch.update(gardenRef, FireBaseKey.GARDEN_GROUP_ID, FieldValue.arrayUnion(user.id))
        }.await()
        currentUser.emit(user)
        emit(Result.Success(garden.id))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateUserInfo(userId: String, user: User): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = usersRef.document(userId).update(
            mapOf(
                "name" to user.name,
                "profileUrl" to user.profileUrl
            )
        ).await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun withdraw(): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }
}