package io.tuttut.data.network.model

import io.tuttut.data.network.constant.GOOGLE_PROFILE_KEY
import java.io.File

data class StorageImage(
    val url: String = "",
    val name: String = ""
) {
    constructor() : this("", "")
}

fun File.toStorageImage(): StorageImage = StorageImage(absolutePath, name)

fun StorageImage.isGoogleProfile(): Boolean = this.url.contains(GOOGLE_PROFILE_KEY)