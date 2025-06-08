package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.JoinGardenRequest
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class JoinGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(joinGardenRequest: JoinGardenRequest): Result<Unit> = kotlin.runCatching {
        gardenRepository.joinGarden(joinGardenRequest).getOrThrow()
        preferenceRepository.setGardenId(joinGardenRequest.gardenId)
    }
}