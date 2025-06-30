package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.image.SaveLocation
import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.usecase.image.DeleteImageUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
) {
    suspend operator fun invoke(): Result<Unit> = runCatchingExceptCancel {
        getCurrentUserUseCase()
            .getOrThrow()
            .let { user ->
                authRepository.withdraw(credential = Credential(user.id, user.gardenId))
                if (user.profile.url.contains("googleusercontent")) {
                    deleteImageUseCase(user.profile, SaveLocation.USER)
                }
            }
            .also {
                clearUserDataUseCase()
            }
    }
}