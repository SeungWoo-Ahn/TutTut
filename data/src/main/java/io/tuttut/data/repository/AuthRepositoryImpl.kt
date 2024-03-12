package io.tuttut.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import io.tuttut.data.constant.FireStoreKey
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.Response
import io.tuttut.data.model.dto.User
import io.tuttut.data.model.context.UserData
import io.tuttut.data.mapper.mapToGarden
import io.tuttut.data.util.getDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val usersRef: CollectionReference,
    private val gardensRef: CollectionReference
): AuthRepository {

    override val searchedGarden = MutableStateFlow<Garden?>(null)

    override suspend fun checkIsNewUser(userId: String): Response<Boolean> = try {
        val documentSnapshot = usersRef.document(userId).get().await()
        val result = documentSnapshot.data == null
        Response.Success(result)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun join(userData: UserData, gardenName: String): Response<Boolean> = try {
        val gardenId = usersRef.document().id
        val user = User(
            id = userData.userId,
            name = userData.userName!!,
            profileUrl = userData.profileUrl,
            gardenId = gardenId,
        )
        val garden = Garden(
            id = gardenId,
            code = gardenId.substring(0, 6),
            name = gardenName,
            created = getDate(),
            groupIdList = listOf(userData.userId),
        )
        val userRef = usersRef.document(user.id)
        val gardenRef = gardensRef.document(gardenId)
        Firebase.firestore.runBatch { batch ->
            batch.set(userRef, user)
            batch.set(gardenRef, garden)
        }.await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun checkGardenExist(gardenCode: String): Response<Boolean> = try {
        val query = gardensRef.whereEqualTo(FireStoreKey.GARDEN_CODE, gardenCode)
        val result = query.get().await()
        if (result.documents.size > 0) {
            searchedGarden.value = result.documents[0].data?.mapToGarden()
            Response.Success(true)
        } else {
            Response.Success(false)
        }
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun joinGarden(userData: UserData): Response<Boolean> = try {
        val gardenId = searchedGarden.value!!.id
        val user = User(
            id = userData.userId,
            name = userData.userName!!,
            profileUrl = userData.profileUrl,
            gardenId = gardenId,
        )
        val userRef = usersRef.document(user.id)
        val gardenRef = gardensRef.document(gardenId)
        Firebase.firestore.runBatch { batch ->
            batch.set(userRef, user)
            batch.update(gardenRef, FireStoreKey.GARDEN_GROUP_ID, FieldValue.arrayUnion(user.id))
        }.await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }
}