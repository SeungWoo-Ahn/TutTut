package io.tuttut.data.mapper

import io.tuttut.data.network.model.StorageImage
import io.tuttut.data.network.model.UserDto
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.domain.model.user.UpdateUserRequest
import io.tuttut.domain.model.user.User

fun UserDto.toDomain(): User =
    User(
        id = id,
        gardenId = gardenId,
        name = name,
        profile = profile.toDomain()
    )

fun JoinRequest.toDto(): UserDto =
    UserDto(
        id = id,
        name = name,
        profile = imageUrl?.let { StorageImage(url = it) } ?: StorageImage()
    )

fun UpdateUserRequest.toUpdateMap(): Map<String, Any?> =
    mapOf(
        "name" to name,
        "profile" to profile.toDto()
    )