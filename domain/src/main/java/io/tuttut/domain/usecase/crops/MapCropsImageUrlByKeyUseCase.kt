package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.usecase.cropsInfo.GetCropsInfoByKeyUseCase
import javax.inject.Inject

class MapCropsImageUrlByKeyUseCase @Inject constructor(
    private val getCropsInfoByKeyUseCase: GetCropsInfoByKeyUseCase,
) {
    suspend operator fun invoke(crops: Crops): Crops {
        return getCropsInfoByKeyUseCase(crops.key)
            .getOrNull()?.let {
                crops.copy(imageUrl = it.imageUrl)
            } ?: crops
    }
}