package io.tuttut.data.mapper

import io.tuttut.data.network.model.UserDto
import io.tuttut.domain.model.user.User

fun UserDto.toDomain(): User =
    User(
        id = id,
        gardenId = gardenId,
        name = name,
        profile = profile.toDomain()
    )
