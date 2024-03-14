package io.tuttut.data.repository.cropsInfo

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface CropsInfoRepository {
    val cropsInfoCached: MutableStateFlow<Boolean>
    val cropsInfoList: MutableStateFlow<List<CropsInfo>>
    val monthlyCropsList: MutableStateFlow<List<CropsInfo>>
    val cropsInfoMap: HashMap<String, CropsInfo>

    suspend fun cachingCropsInfo(currentMonth: Int): Response<Boolean>

    fun getCropsInfoList(currentMonth: Int): Flow<Result<DocumentReference>>
}