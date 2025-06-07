package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.CreateGardenRequest
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class CreateGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(createGardenRequest: CreateGardenRequest): Result<Unit> = runCatching {
        val gardenId = gardenRepository.createGarden(createGardenRequest).getOrThrow()
        preferenceRepository.setGardenId(gardenId)
    }
}