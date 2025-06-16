package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<Garden> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        gardenRepository.getGarden(credential.gardenId)
    }
}