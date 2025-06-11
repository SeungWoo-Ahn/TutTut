package io.tuttut.domain.usecase.user

import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class GetUserAndSaveGardenIdUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> = runCatchingExceptCancel {
        val user = getUserUseCase(id).getOrThrow()
        preferenceRepository.setGardenId(user.gardenId)
    }
}