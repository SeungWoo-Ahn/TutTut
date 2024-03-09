package io.tuttut.data.repository

import io.tuttut.data.model.Response

interface AuthRepository {
     suspend fun checkIsNewUser(userId: String): Response<Boolean>
}