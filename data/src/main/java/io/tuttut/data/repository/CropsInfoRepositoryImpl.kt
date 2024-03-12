package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropsInfoRepositoryImpl @Inject constructor(
    private val cropsInfoRef: CollectionReference
): CropsInfoRepository {

    override suspend fun addCropsInfoByAdmin(cropsInfo: CropsInfo): Response<Boolean> = try {
        cropsInfoRef.document(cropsInfo.key).set(cropsInfo)
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }
}