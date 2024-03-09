package io.tuttut.data.repository

import io.tuttut.data.model.Response
import io.tuttut.data.model.UserData

interface AuthRepository {
     suspend fun checkIsNewUser(userId: String): Response<Boolean>

     suspend fun join(userData: UserData, gardenName: String): Response<Boolean>
}