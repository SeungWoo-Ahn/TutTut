package io.tuttut.data.repository.cropsInfo

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.isRecommended
import io.tuttut.data.model.response.Result
import io.tuttut.data.util.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Named

class CropsInfoRepositoryImpl @Inject constructor(
    @Named("cropsInfoRef") private val cropsInfoRef: CollectionReference
): CropsInfoRepository {
    override val cropsInfoList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val monthlyCropsList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val cropsInfoMap: HashMap<String, CropsInfo> = HashMap()

    override fun getCropsInfoList(currentMonth: Int): Flow<Result<List<CropsInfo>>> = cropsInfoRef.asFlow(CropsInfo::class.java) {
        cropsInfoList.value = it
        monthlyCropsList.value = it.flatMap { cropsInfo ->
            cropsInfo.plantingSeasons.filter { season ->
                season.isRecommended(currentMonth)
            }.map { cropsInfo }
        }
        for (cropsInfo in it) {
            cropsInfoMap[cropsInfo.key] = cropsInfo
        }
    }


    /*override suspend fun cachingCropsInfo(currentMonth: Int): Response<Boolean> = try {
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
    }*/

}