package io.tuttut.data.model.dto

import io.tuttut.data.constant.DEFAULT_USER_IMAGE

data class User(
    val id: String = "",
    val gardenId: String = "",
    val name: String = "",
    val profile: StorageImage = StorageImage(url = DEFAULT_USER_IMAGE),
)
