package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class GetGardenByCodeUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
) {
    suspend operator fun invoke(code: String): Result<Garden> = runCatchingExceptCancel {
        gardenRepository.getGardenByCode(code)
    }
}
