package io.tuttut.domain.usecase.garden

import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.user.ClearUserDataUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LeaveGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
    private val clearUserDataUseCase: ClearUserDataUseCase,
) {
    suspend operator fun invoke(): Result<Unit> = runCatchingExceptCancel {
        preferenceRepository
            .getCredentialFlow()
            .first()
            .let { credential ->
                gardenRepository.leaveGarden(credential)
            }
            .also {
                clearUserDataUseCase()
            }
    }
}