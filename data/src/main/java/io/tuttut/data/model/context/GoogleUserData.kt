package io.tuttut.data.model.context

import io.tuttut.data.network.constant.DEFAULT_IMAGE_NAME
import io.tuttut.data.network.constant.DEFAULT_USER_IMAGE
import io.tuttut.data.network.constant.DEFAULT_USER_NAME
import io.tuttut.data.network.model.StorageImage
import io.tuttut.data.network.model.User

data class UserData(
    val userId: String = "",
    val userName: String? = null,
    val profileUrl: String? = null
)

fun UserData.toUser(): User = User(
    id = userId,
    name = userName ?: DEFAULT_USER_NAME,
    profile = StorageImage(
        url = profileUrl ?: DEFAULT_USER_IMAGE,
        name = DEFAULT_IMAGE_NAME
    )
)