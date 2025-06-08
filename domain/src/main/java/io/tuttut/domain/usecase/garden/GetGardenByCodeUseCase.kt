package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.repository.GardenRepository
import javax.inject.Inject

class GetGardenByCodeUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
) {
    suspend operator fun invoke(code: String): Result<Garden> =
        gardenRepository.getGardenByCode(code)
}
