package io.tuttut.data.model.dto

import java.io.File

data class StorageImage(
    val url: String = "",
    val name: String = ""
) {
    constructor() : this("", "")
}

fun File.toStorageImage(): StorageImage = StorageImage(absolutePath, name)
