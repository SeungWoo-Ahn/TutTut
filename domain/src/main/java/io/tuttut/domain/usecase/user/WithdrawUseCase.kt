package io.tuttut.domain.usecase.user

import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<Unit> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        authRepository.withdraw(credential)
            .also { preferenceRepository.clearUserData() }
    }
}