package io.tuttut.domain.model.user

import io.tuttut.domain.model.image.ImageSource

data class User(
    val id: String,
    val gardenId: String,
    val name: String,
    val profile: ImageSource.Remote,
)
