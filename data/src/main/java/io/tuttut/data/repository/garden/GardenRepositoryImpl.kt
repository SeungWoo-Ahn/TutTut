package io.tuttut.data.repository.garden

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.Garden
import io.tuttut.data.network.model.User
import io.tuttut.data.network.model.toMap
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asResultFlow
import io.tuttut.data.util.asSnapShotFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class GardenRepositoryImpl @Inject constructor(
    @Named("usersRef") val usersRef: CollectionReference,
    @Named("gardensRef") val gardensRef: CollectionReference,
) : GardenRepository {
    override val currentGarden: MutableStateFlow<Garden> = MutableStateFlow(Garden())
    override val gardenMemberInfo: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    override val gardenMemberMap: HashMap<String, User> = HashMap()
    override fun checkGardenExist(gardenCode: String): Flow<Result<List<Garden>>>
        = gardensRef.whereEqualTo(FirebaseKey.GARDEN_CODE, gardenCode).asResultFlow(Garden::class.java)

    override fun createGarden(userId: String, gardenName: String, created: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val gardenId = gardensRef.document().id
        val garden = Garden(
            id = gardenId,
            code = gardenId.substring(0, 6),
            name = gardenName,
            created = created,
            groupIdList = listOf(userId)
        )
        val ref = gardensRef.document(gardenId)
        val userRef = usersRef.document(userId)
        Firebase.firestore.runBatch { batch ->
            batch.set(ref, garden)
            batch.update(userRef, FirebaseKey.USER_GARDEN_ID, gardenId)
        }.await()
        emit(Result.Success(gardenId))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun joinGarden(userId: String, gardenId: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val ref = gardensRef.document(gardenId)
        val userRef = usersRef.document(userId)
        Firebase.firestore.runBatch { batch ->
            batch.update(ref, FirebaseKey.GARDEN_GROUP_ID, FieldValue.arrayUnion(userId))
            batch.update(userRef, FirebaseKey.USER_GARDEN_ID, gardenId)
        }.await()
        emit(Result.Success(gardenId))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun getGardenInfo(gardenId: String): Flow<Garden>
        = gardensRef.document(gardenId).asSnapShotFlow(Garden::class.java) {
            currentGarden.value = it
        }

    override suspend fun getGardenMemberInfo(gardenId: String): Flow<Boolean> = flow {
        val garden = gardensRef.document(gardenId).get().await().toObject(Garden::class.java)
        val userList = mutableListOf<User>()
        garden?.groupIdList?.forEach { id ->
            val user = usersRef.document(id).get().await().toObject(User::class.java)
            user?.let {
                userList.add(it)
                gardenMemberMap[it.id] = it
            }
            gardenMemberInfo.value = userList
        }
        emit(true)
    }.catch {
        emit(false)
    }.flowOn(Dispatchers.IO)

    override fun updateGardenInfo(
        garden: Garden
    ): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = gardensRef.document(garden.id).update(garden.toMap()).await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun quitGarden(userId: String, gardenId: String): Flow<Result<DocumentReference>> = flow {
        emit(Result.Loading)
        val ref = gardensRef.document(gardenId)
        val userRef = usersRef.document(userId)
        Firebase.firestore.runBatch { batch ->
            batch.update(ref, FirebaseKey.GARDEN_GROUP_ID, FieldValue.arrayRemove(userId))
            batch.update(userRef, FirebaseKey.USER_GARDEN_ID, "")
        }
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}