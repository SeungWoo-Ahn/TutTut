package io.tuttut.domain.repository

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.cropsInfo.Recipe
import kotlinx.coroutines.flow.Flow

interface CropsInfoRepository {
    suspend fun getCropsInfoList(): Result<List<CropsInfo>>

    suspend fun getCropsInfoByKey(cropsKey: CropsKey): Result<CropsInfo>

    fun getCropsRecipeList(keyword: String): Flow<List<Recipe>>
}