package io.tuttut.domain.usecase.user

import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class ClearUserDataUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke() {
        preferenceRepository.clearUserData()
    }
}