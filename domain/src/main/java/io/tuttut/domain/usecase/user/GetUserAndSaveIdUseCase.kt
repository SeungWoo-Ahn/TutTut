package io.tuttut.domain.usecase.user

import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class GetUserAndSaveIdUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> = runCatchingExceptCancel {
        getUserUseCase(id)
            .getOrThrow()
            .also { user ->
                preferenceRepository.setUserId(user.id)
                preferenceRepository.setGardenId(user.gardenId)
            }
    }
}