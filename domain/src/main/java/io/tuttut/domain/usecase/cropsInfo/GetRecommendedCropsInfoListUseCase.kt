package io.tuttut.domain.usecase.cropsInfo

import io.tuttut.domain.model.cropsInfo.CropsInfo
import javax.inject.Inject

class GetRecommendedCropsInfoListUseCase @Inject constructor(
    private val getCropsInfoListUseCase: GetCropsInfoListUseCase,
) {
    suspend operator fun invoke(month: Int): Result<List<CropsInfo>> = runCatching {
        getCropsInfoListUseCase()
            .getOrThrow()
            .filter { cropsInfo ->
                cropsInfo.plantingSeasons.any { (start, end) ->
                    month in start.toMonth()..end.toMonth()
                }
        }
    }

    private fun Int.toMonth(): Int {
        return if (this % 2 == 0)  this / 2
        else this / 2 + 1
    }
}