package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(id: String): Result<User> = runCatching {
        val cachedUser = preferenceRepository.getGardenUserById(id)
        if (cachedUser != null) {
            return@runCatching cachedUser
        }
        authRepository.getUser(id)
            .getOrThrow()
            .also { user -> preferenceRepository.setGardenUser(user) }
    }
}