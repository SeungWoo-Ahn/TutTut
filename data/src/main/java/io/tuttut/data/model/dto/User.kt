package io.tuttut.data.model.dto

import io.tuttut.data.constant.DEFAULT_IMAGE_NAME
import io.tuttut.data.constant.DEFAULT_USER_IMAGE

data class User(
    val id: String = "",
    val gardenId: String = "",
    val name: String = "",
    val profile: StorageImage = StorageImage(DEFAULT_USER_IMAGE, DEFAULT_IMAGE_NAME),
)

fun User.toMap(): Map<String, Any?> = hashMapOf(
    "name" to name,
    "profile" to profile
)