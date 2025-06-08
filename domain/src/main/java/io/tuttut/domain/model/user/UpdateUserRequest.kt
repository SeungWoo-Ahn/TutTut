package io.tuttut.domain.model.user

import io.tuttut.domain.model.image.ImageSource

data class UpdateUserRequest(
    val name: String,
    val profile: ImageSource.Remote,
)
