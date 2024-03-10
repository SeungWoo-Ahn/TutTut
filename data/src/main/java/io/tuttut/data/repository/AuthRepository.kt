package io.tuttut.data.repository

import io.tuttut.data.model.dto.Response
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.dto.Garden
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {

     val searchedGarden: MutableStateFlow<Garden?>

     suspend fun checkIsNewUser(userId: String): Response<Boolean>

     suspend fun join(userData: UserData, gardenName: String): Response<Boolean>

     suspend fun checkGardenExist(gardenCode: String): Response<Boolean>

     suspend fun joinGarden(gardenId: String): Response<Garden>
}