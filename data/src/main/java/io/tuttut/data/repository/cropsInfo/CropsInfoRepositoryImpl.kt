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
    @Named("cropsInfoRef") val cropsInfoRef: CollectionReference
): CropsInfoRepository {
    override val cropsInfoList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val monthlyCropsList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val cropsInfoMap: HashMap<String, CropsInfo> = HashMap()

    override fun getCropsInfoList(currentMonth: Int): Flow<Result<List<CropsInfo>>> = cropsInfoRef.asFlow(CropsInfo::class.java) {
        if (cropsInfoList.value.isEmpty()) {
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
    }

}