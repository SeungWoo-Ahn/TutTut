package io.tuttut.data.repository.garden

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.dto.Garden
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor() : GardenRepository {

    override fun getGardenInfo(gardenId: String): Flow<Result<Garden>> {
        TODO("Not yet implemented")
    }

    override fun joinGarden(userData: UserData): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun updateGardenInfo(
        gardenId: String,
        garden: Garden
    ): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun deleteGardenInfo(gardenId: String): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }
}