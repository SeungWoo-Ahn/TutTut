package io.tuttut.domain.usecase.garden

import io.tuttut.domain.model.garden.GardenWithMember
import io.tuttut.domain.usecase.user.GetGardenUserUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class GetGardenWithMemberUseCase @Inject constructor(
    private val getGardenUseCase: GetGardenUseCase,
    private val getGardenUserUseCase: GetGardenUserUseCase,
) {
    suspend operator fun invoke(): Result<GardenWithMember> = runCatchingExceptCancel {
        getGardenUseCase()
            .getOrThrow()
            .let { garden ->
                GardenWithMember(
                    code = garden.code,
                    name = garden.name,
                    memberList = garden.groupIdList.mapNotNull { id ->
                        getGardenUserUseCase(id, garden.id)
                    }
                )
            }
    }
}