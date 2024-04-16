package io.tuttut.data.model.dto

data class User(
    val id: String = "",
    val gardenId: String = "",
    val name: String = "",
    val profile: StorageImage = StorageImage(),
)

fun User.toMap(): Map<String, Any?> = hashMapOf(
    "name" to name,
    "profile" to profile
)