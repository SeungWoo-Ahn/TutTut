package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.User
import io.tuttut.domain.usecase.garden.GetGardenUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class GetGardenUserListUseCase @Inject constructor(
    private val getGardenUserUseCase: GetGardenUserUseCase,
    private val getGardenUseCase: GetGardenUseCase,
) {
    suspend operator fun invoke(): Result<List<User>> = runCatchingExceptCancel {
        getGardenUseCase()
            .getOrThrow()
            .let { garden ->
                garden
                    .groupIdList
                    .mapNotNull { id ->
                        getGardenUserUseCase(id, garden.id)
                    }
            }
    }
}