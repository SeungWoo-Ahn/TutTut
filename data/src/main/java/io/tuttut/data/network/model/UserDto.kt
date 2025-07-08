package io.tuttut.data.network.model

data class UserDto(
    val id: String = "",
    val gardenId: String = "",
    val name: String = "",
    val profile: StorageImage = StorageImage(),
)
