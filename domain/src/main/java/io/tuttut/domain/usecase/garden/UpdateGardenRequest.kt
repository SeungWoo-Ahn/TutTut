package io.tuttut.domain.usecase.garden

import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateGardenRequest @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(name: String): Result<Unit> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        gardenRepository.updateGarden(credential.gardenId, name)
    }
}