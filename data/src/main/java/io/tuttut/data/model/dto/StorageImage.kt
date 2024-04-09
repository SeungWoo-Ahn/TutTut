package io.tuttut.data.model.dto

data class StorageImage(
    val url: String = "",
    val name: String = ""
) {
    constructor() : this("", "")
}
