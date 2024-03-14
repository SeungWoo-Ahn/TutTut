package io.tuttut.data.repository.cropsInfo

import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Response
import kotlinx.coroutines.flow.MutableStateFlow

interface CropsInfoRepository {
    val cropsInfoCached: MutableStateFlow<Boolean>
    val cropsInfoList: MutableStateFlow<List<CropsInfo>>
    val monthlyCropsList: MutableStateFlow<List<CropsInfo>>
    val cropsInfoMap: HashMap<String, CropsInfo>

    suspend fun addCropsInfoByAdmin(cropsInfo: CropsInfo): Response<Boolean>

    suspend fun cachingCropsInfo(currentMonth: Int): Response<Boolean>
}