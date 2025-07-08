package io.tuttut.domain.repository

import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User

interface AuthRepository {
    suspend fun getUser(id: String): User

    suspend fun join(joinRequest: JoinRequest)

    suspend fun updateUser(id: String, updateUserRequest: UpdateUserRequest)

    suspend fun withdraw(credential: Credential)
}