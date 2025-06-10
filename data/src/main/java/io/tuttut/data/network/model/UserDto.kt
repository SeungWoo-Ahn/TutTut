package io.tuttut.data.network.model

import com.google.firebase.firestore.DocumentId

data class UserDto(
    @DocumentId
    val id: String = "",
    val gardenId: String = "",
    val name: String = "",
    val profile: StorageImage = StorageImage(),
)

fun UserDto.toMap(): Map<String, Any?> = hashMapOf(
    "name" to name,
    "profile" to profile
)