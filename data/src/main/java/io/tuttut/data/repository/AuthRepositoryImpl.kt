package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.model.Response
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val usersRef: CollectionReference
): AuthRepository {
    override suspend fun checkIsNewUser(userId: String): Response<Boolean> = try {
        val documentSnapshot = usersRef.document(userId).get().await()
        val result = documentSnapshot.data == null
        Response.Success(result)
    } catch (e: Exception) {
        Response.Failure(e)
    }
}