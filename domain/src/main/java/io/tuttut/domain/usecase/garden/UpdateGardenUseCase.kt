package io.tuttut.domain.usecase.garden

import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(name: String): Result<Unit> = runCatchingExceptCancel {
        preferenceRepository
            .getCredentialFlow()
            .first()
            .let { credential ->
                gardenRepository.updateGarden(credential.gardenId, name)
            }
    }
}