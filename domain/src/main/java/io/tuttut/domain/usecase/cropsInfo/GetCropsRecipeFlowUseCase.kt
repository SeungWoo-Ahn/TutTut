package io.tuttut.domain.usecase.cropsInfo

import io.tuttut.domain.model.cropsInfo.Recipe
import io.tuttut.domain.repository.CropsInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetCropsRecipeFlowUseCase @Inject constructor(
    private val cropsInfoRepository: CropsInfoRepository,
) {
    operator fun invoke(keyword: String): Flow<List<Recipe>> =
        cropsInfoRepository
            .getCropsRecipeList(keyword)
            .catch {
                emit(emptyList())
            }
}