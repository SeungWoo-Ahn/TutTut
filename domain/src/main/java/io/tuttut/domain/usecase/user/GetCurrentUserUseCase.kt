package io.tuttut.domain.usecase.user

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<User> = runCatching {
        val cachedUser = preferenceRepository.getCurrentUser()
        if (cachedUser != null) {
            return@runCatching cachedUser
        }
        val userId = preferenceRepository.getUserIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        getUserUseCase(userId)
            .getOrThrow()
            .also { user -> preferenceRepository.setCurrentUser(user) }
    }
}