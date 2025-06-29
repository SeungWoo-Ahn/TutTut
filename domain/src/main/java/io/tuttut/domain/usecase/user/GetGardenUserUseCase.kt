package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetGardenUserUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(id: String, gardenId: String): User? {
        val user = getUserUseCase(id).getOrDefault(null)
        return if (user?.gardenId == gardenId) {
            preferenceRepository.setGardenUser(user)
            user
        } else {
            null
        }
    }
}