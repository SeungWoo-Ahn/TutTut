package io.tuttut.domain.usecase.user

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.user.User
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.garden.GetGardenUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetGardenUserListUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getGardenUseCase: GetGardenUseCase,
    private val preferenceRepository: PreferenceRepository,

) {
    suspend operator fun invoke(): Result<List<User>> = runCatching {
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        getGardenUseCase(gardenId)
            .getOrThrow()
            .groupIdList
            .mapNotNull { id -> getUserUseCase(id).getOrNull() }
    }
}