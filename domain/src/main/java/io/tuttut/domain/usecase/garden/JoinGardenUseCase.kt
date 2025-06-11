package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class JoinGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(credential: Credential): Result<Unit> = runCatchingExceptCancel {
        gardenRepository.joinGarden(credential)
        preferenceRepository.setGardenId(credential.gardenId)
    }
}