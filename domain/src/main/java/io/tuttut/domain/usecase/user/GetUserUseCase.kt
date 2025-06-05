package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(id: String): Result<User> =
        authRepository.getUser(id)
}