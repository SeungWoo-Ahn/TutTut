package io.tuttut.data.repository.garden

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.constant.FireBaseKey
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User
import io.tuttut.data.model.dto.toMap
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asResultFlow
import io.tuttut.data.util.asSnapShotFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
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
        = gardensRef.whereEqualTo(FireBaseKey.GARDEN_CODE, gardenCode).asResultFlow(Garden::class.java).take(1)

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

    override fun deleteGardenInfo(gardenId: String): Flow<Result<Void>> {
        TODO("Not yet implemented")
    }
}