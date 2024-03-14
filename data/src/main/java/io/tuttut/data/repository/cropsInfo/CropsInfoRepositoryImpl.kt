package io.tuttut.data.repository.cropsInfo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Response
import io.tuttut.data.model.dto.isRecommended
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CropsInfoRepositoryImpl @Inject constructor(
    private val cropsInfoRef: CollectionReference
): CropsInfoRepository {
    override val cropsInfoCached: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val cropsInfoList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val monthlyCropsList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val cropsInfoMap: HashMap<String, CropsInfo> = HashMap()

    override suspend fun cachingCropsInfo(currentMonth: Int): Response<Boolean> = try {
        if (!cropsInfoCached.value) {
            val result = cropsInfoRef.get().await().documents
            if (result.size > 0) {
                val totalCropsList = result.mapNotNull { doc ->
                    doc.toObject(CropsInfo::class.java)?.also { cropsInfo ->
                        cropsInfoMap[cropsInfo.key] = cropsInfo
                    }
                }.toList()
                val monthlyRecommend = totalCropsList.flatMap { cropsInfo ->
                    cropsInfo.plantingSeasons.filter { season ->
                        season.isRecommended(currentMonth)
                    }.map { cropsInfo }
                }.toList()
                cropsInfoList.value = totalCropsList
                monthlyCropsList.value = monthlyRecommend
                cropsInfoCached.value = true
                Response.Success(true)
            } else {
                cropsInfoCached.value = false
                Response.Success(false)
            }
        } else {
            cropsInfoCached.value = true
            Response.Success(true)
        }
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override fun getCropsInfoList(currentMonth: Int): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }
}