package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.CreateGardenRequest
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class CreateGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(createGardenRequest: CreateGardenRequest): Result<Unit> = runCatchingExceptCancel {
        gardenRepository.createGarden(createGardenRequest)
            .also { gardenId -> preferenceRepository.setGardenId(gardenId) }
    }
}