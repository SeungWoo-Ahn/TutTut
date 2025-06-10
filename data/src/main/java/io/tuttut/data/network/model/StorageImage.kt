package io.tuttut.data.network.model

import io.tuttut.data.network.constant.DEFAULT_IMAGE_NAME
import io.tuttut.data.network.constant.DEFAULT_USER_IMAGE
import io.tuttut.data.network.constant.GOOGLE_PROFILE_KEY
import java.io.File

data class StorageImage(
    val url: String = DEFAULT_USER_IMAGE,
    val name: String = DEFAULT_IMAGE_NAME
)

fun File.toStorageImage(): StorageImage = StorageImage(absolutePath, name)

fun StorageImage.isGoogleProfile(): Boolean = this.url.contains(GOOGLE_PROFILE_KEY)