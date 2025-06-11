package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(id: String): Result<User> = runCatchingExceptCancel {
        val cachedUser = preferenceRepository.getGardenUserById(id)
        if (cachedUser != null) {
            return@runCatchingExceptCancel cachedUser
        }
        authRepository.getUser(id)
            .also { user -> preferenceRepository.setGardenUser(user) }
    }
}