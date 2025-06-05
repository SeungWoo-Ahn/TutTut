package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.repository.GardenRepository
import javax.inject.Inject

class GetGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
) {
    suspend operator fun invoke(id: String): Result<Garden> =
        gardenRepository.getGarden(id)
}