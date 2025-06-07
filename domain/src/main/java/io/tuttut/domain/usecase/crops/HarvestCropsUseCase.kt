package io.tuttut.domain.usecase.crops

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.crops.HarvestCropsRequest
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HarvestCropsUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(harvestCropsRequest: HarvestCropsRequest): Result<Unit> = runCatching {
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        cropsRepository.harvestCrops(gardenId, harvestCropsRequest)
    }
}