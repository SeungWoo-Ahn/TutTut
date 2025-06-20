package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class JoinUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(joinRequest: JoinRequest): Result<Unit> = runCatching {
        authRepository.join(joinRequest).getOrThrow()
        preferenceRepository.setUserId(joinRequest.id)
    }
}