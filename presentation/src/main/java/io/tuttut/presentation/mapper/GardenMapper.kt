package io.tuttut.presentation.mapper

import io.tuttut.domain.model.garden.GardenWithMember
import io.tuttut.domain.model.user.User
import io.tuttut.presentation.model.GardenUiModel

fun GardenWithMember.toUiModel(): GardenUiModel =
    GardenUiModel(
        name = name,
        code = code,
        memberList = memberList.map(User::toUiModel)
    )