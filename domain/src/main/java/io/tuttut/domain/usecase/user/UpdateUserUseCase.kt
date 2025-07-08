package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.image.SaveLocation
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.AuthRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.image.DeleteImageUseCase
import io.tuttut.domain.usecase.image.UploadImageUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
) {
    suspend operator fun invoke(name: String, imageSource: ImageSource): Result<Unit> = runCatchingExceptCancel {
        val currentUser = getCurrentUserUseCase().getOrThrow()
        if (isUserDataChanged(currentUser, name, imageSource)) {
            val credential = preferenceRepository.getCredentialFlow().first()
            val profile = uploadImageUseCase(imageSource, SaveLocation.USER).getOrThrow()
            val updateUserRequest = UpdateUserRequest(name, profile)
            authRepository.updateUser(credential.userId, updateUserRequest)
                .also {
                    deleteImageUseCase(currentUser.profile, SaveLocation.USER)
                    currentUser.copy(name = name, profile = profile).let { updatedUser ->
                        preferenceRepository.setCurrentUser(updatedUser)
                        preferenceRepository.setGardenUser(updatedUser)
                    }
                }
        }
    }

    private fun isUserDataChanged(user: User, name: String, imageSource: ImageSource): Boolean =
        user.name != name || user.profile != imageSource
}