package io.tuttut.domain.usecase.user

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val userId = preferenceRepository.getUserIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        authRepository.withdraw(userId, gardenId)
            .getOrThrow()
            .also { preferenceRepository.clearUserData() }
    }
}