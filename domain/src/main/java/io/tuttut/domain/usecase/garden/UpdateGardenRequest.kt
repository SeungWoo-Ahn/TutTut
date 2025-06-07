package io.tuttut.domain.usecase.garden

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.garden.UpdateGardenRequest
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateGardenRequest @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(updateGardenRequest: UpdateGardenRequest): Result<Unit> = runCatching {
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        gardenRepository.updateGarden(gardenId, updateGardenRequest)
    }
}