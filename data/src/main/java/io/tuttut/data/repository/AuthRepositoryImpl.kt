package io.tuttut.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import io.tuttut.data.model.Garden
import io.tuttut.data.model.Response
import io.tuttut.data.model.User
import io.tuttut.data.model.UserData
import io.tuttut.data.util.getDate
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val usersRef: CollectionReference,
    private val gardensRef: CollectionReference
): AuthRepository {

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
            cropsIdList = emptyList()
        )
        val garden = Garden(
            id = gardenId,
            code = gardenId.substring(0, 6),
            name = gardenName,
            created = getDate(),
            groupIdList = listOf(userData.userId),
            cropsList = emptyList()
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
}