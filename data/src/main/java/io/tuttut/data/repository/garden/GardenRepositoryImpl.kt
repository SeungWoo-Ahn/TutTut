package io.tuttut.data.repository.garden

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.constant.FireBaseKey
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
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
    override val gardenMemberInfo: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    override val gardenMemberMap: HashMap<String, User> = HashMap()
    override fun checkGardenExist(gardenCode: String): Flow<Result<List<Garden>>>
        = gardensRef.whereEqualTo(FireBaseKey.GARDEN_CODE, gardenCode).asFlow(Garden::class.java)

    override fun getGardenInfo(gardenId: String): Flow<Garden>
        = gardensRef.document(gardenId).asSnapShotFlow(Garden::class.java)

    override fun getGardenMemberInfo(gardenId: String): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        val userList = mutableListOf<User>()
        val garden = gardensRef.document(gardenId).get().await().toObject(Garden::class.java)
        if (garden == null) {
            emit(Result.NotFound)
        } else {
            garden.groupIdList.forEach { id ->
                val user = usersRef.document(id).get().await().toObject(User::class.java)
                user?.let {
                    userList.add(it)
                    gardenMemberMap[it.id] = it
                }
            }
        }
        gardenMemberInfo.value = userList
        emit(Result.Success(true))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun updateGardenInfo(
        garden: Garden
    ): Flow<Result<Void>> = flow {
        emit(Result.Loading)
        val ref = gardensRef.document(garden.id).update(
            mapOf(
                "name" to garden.name
            )
        ).await()
        emit(Result.Success(ref))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteGardenInfo(gardenId: String): Flow<Result<Void>> {
        TODO("Not yet implemented")
    }
}