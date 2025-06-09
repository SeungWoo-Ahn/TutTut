package io.tuttut.data.network.model

import com.google.firebase.firestore.DocumentId
import io.tuttut.data.network.constant.DEFAULT_USER_IMAGE

data class UserDto(
    @DocumentId
    val id: String = "",
    val gardenId: String = "",
    val name: String = "",
    val profile: StorageImage = StorageImage(url = DEFAULT_USER_IMAGE),
)

fun UserDto.toMap(): Map<String, Any?> = hashMapOf(
    "name" to name,
    "profile" to profile
)