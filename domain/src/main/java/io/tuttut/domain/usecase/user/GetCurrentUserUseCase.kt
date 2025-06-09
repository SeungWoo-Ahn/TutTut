package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<User> = runCatchingExceptCancel {
        val cachedUser = preferenceRepository.getCurrentUser()
        if (cachedUser != null) {
            return@runCatchingExceptCancel cachedUser
        }
        val credential = preferenceRepository.getCredentialFlow().first()
        getUserUseCase(credential.userId)
            .getOrThrow()
            .also { user -> preferenceRepository.setCurrentUser(user) }
    }
}