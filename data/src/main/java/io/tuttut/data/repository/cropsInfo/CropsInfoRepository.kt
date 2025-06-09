package io.tuttut.data.repository.cropsInfo

import io.tuttut.data.network.model.CropsInfoDto
import io.tuttut.data.network.model.RecipeDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface CropsInfoRepository {
    val cropsInfoList: MutableStateFlow<List<CropsInfoDto>>
    val monthlyCropsList: MutableStateFlow<List<CropsInfoDto>>
    val cropsInfoMap: HashMap<String, CropsInfoDto>

    fun getCropsInfoList(currentMonth: Int): Flow<List<CropsInfoDto>>

    fun getCropsRecipes(keyword: String): Flow<List<RecipeDto>>
}