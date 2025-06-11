package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.tuttut.data.mapper.toDomain
import io.tuttut.data.mapper.toDto
import io.tuttut.data.mapper.toUpdateMap
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.UserDto
import io.tuttut.data.network.di.FireStoreDB
import io.tuttut.data.network.di.GardensReference
import io.tuttut.data.network.di.UsersReference
import io.tuttut.data.util.getOneShot
import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @FireStoreDB val db: FirebaseFirestore,
    @UsersReference val usersRef: CollectionReference,
    @GardensReference val gardensRef: CollectionReference,
) : AuthRepository {
    override suspend fun getUser(id: String): User {
        val userDto = usersRef.document(id).getOneShot<UserDto>()
        if (userDto.gardenId.isEmpty()) {
            throw ExceptionBoundary.GardenNotFound()
        }
        return userDto.toDomain()
    }

    override suspend fun join(joinRequest: JoinRequest) {
        val userDto = joinRequest.toDto()
        usersRef.document(userDto.id).set(userDto).await()
    }

    override suspend fun updateUser(id: String, updateUserRequest: UpdateUserRequest) {
        val updateMap = updateUserRequest.toUpdateMap()
        usersRef.document(id).update(updateMap).await()
    }

    override suspend fun withdraw(credential: Credential) {
        val userDoc = usersRef.document(credential.userId)
        val gardenDoc = gardensRef.document(credential.gardenId)
        db.runBatch { batch ->
            batch.delete(userDoc)
            batch.update(
                gardenDoc,
                FirebaseKey.GARDEN_GROUP_ID,
                FieldValue.arrayRemove(credential.userId)
            )
        }.await()
    }
}