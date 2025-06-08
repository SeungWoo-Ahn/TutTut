package io.tuttut.domain.usecase.garden

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.garden.LeaveGardenRequest
import io.tuttut.domain.repository.GardenRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LeaveGardenUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val userId = preferenceRepository.getUserIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val leaveGardenRequest = LeaveGardenRequest(userId, gardenId)
        gardenRepository.leaveGarden(leaveGardenRequest)
        preferenceRepository.clearUserData()
    }

}