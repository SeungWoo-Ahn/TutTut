package io.tuttut.data.repository.cropsInfo

import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import io.tuttut.data.model.response.Result

interface CropsInfoRepository {
    val cropsInfoList: MutableStateFlow<List<CropsInfo>>
    val monthlyCropsList: MutableStateFlow<List<CropsInfo>>
    val cropsInfoMap: HashMap<String, CropsInfo>

    fun getCropsInfoList(currentMonth: Int): Flow<Result<List<CropsInfo>>>

    fun getCropsRecipes(keyword: String): Flow<List<Recipe>>
}