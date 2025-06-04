package io.tuttut.domain.repository

import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User

interface AuthRepository {
    suspend fun getUser(id: String): Result<User>

    suspend fun join(joinRequest: JoinRequest): Result<Unit>

    suspend fun updateUser(updateUserRequest: UpdateUserRequest): Result<Unit>

    suspend fun withdraw(id: String, gardenId: String): Result<Unit>
}