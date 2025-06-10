package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.tuttut.data.mapper.toDomain
import io.tuttut.data.mapper.toDto
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.GardenDto
import io.tuttut.data.network.di.FireStoreDB
import io.tuttut.data.network.di.GardensReference
import io.tuttut.data.network.di.UsersReference
import io.tuttut.data.util.getOneShot
import io.tuttut.domain.model.garden.CreateGardenRequest
import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.repository.GardenRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor(
    @FireStoreDB val db: FirebaseFirestore,
    @UsersReference val usersRef: CollectionReference,
    @GardensReference val gardensRef: CollectionReference,
) : GardenRepository {
    override suspend fun getGarden(id: String): Garden =
        gardensRef
            .document(id)
            .getOneShot<GardenDto>()
            .toDomain()

    override suspend fun getGardenByCode(code: String): Garden =
        gardensRef
            .whereEqualTo(FirebaseKey.GARDEN_CODE, code)
            .getOneShot<GardenDto>()
            .first()
            .toDomain()

    override suspend fun createGarden(createGardenRequest: CreateGardenRequest): String {
        val id = gardensRef.document().id
        val gardenDto = createGardenRequest.toDto(id)
        gardensRef.document(id).set(gardenDto).await()
        return id
    }

    override suspend fun joinGarden(credential: Credential) {
        val userDoc = usersRef.document(credential.userId)
        val gardenDoc = gardensRef.document(credential.gardenId)
        db.runBatch { batch ->
            batch.update(
                userDoc,
                FirebaseKey.USER_GARDEN_ID,
                credential.gardenId
            )
            batch.update(
                gardenDoc,
                FirebaseKey.GARDEN_GROUP_ID,
                FieldValue.arrayUnion(credential.userId)
            )
        }.await()
    }

    override suspend fun updateGarden(id: String, name: String) {
        val updateMap = mapOf("name" to name)
        gardensRef.document(id).update(updateMap).await()
    }

    override suspend fun leaveGarden(credential: Credential) {
        val usersDoc = usersRef.document(credential.userId)
        val gardenDoc = gardensRef.document(credential.gardenId)
        db.runBatch { batch ->
            batch.update(
                usersDoc,
                FirebaseKey.USER_GARDEN_ID,
                ""
            )
            batch.update(
                gardenDoc,
                FirebaseKey.GARDEN_GROUP_ID,
                FieldValue.arrayRemove(credential.userId)
            )
        }.await()
    }
}