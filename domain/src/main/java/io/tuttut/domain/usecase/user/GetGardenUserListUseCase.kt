package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.garden.GetGardenUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetGardenUserListUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getGardenUseCase: GetGardenUseCase,
    private val preferenceRepository: PreferenceRepository,

) {
    suspend operator fun invoke(): Result<List<User>> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        getGardenUseCase(credential.gardenId)
            .getOrThrow()
            .groupIdList
            .mapNotNull { id -> getUserUseCase(id).getOrNull() }
    }
}