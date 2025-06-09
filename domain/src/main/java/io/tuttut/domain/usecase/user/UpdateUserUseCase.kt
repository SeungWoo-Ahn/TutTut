package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.image.UploadImageUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase,
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(name: String, imageSource: ImageSource): Result<Unit> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        val profile = uploadImageUseCase(imageSource).getOrThrow()
        val updateUserRequest = UpdateUserRequest(name, profile)
        authRepository.updateUser(credential.userId, updateUserRequest)
            .also { preferenceRepository.updateCurrentUser(updateUserRequest) }
    }
}