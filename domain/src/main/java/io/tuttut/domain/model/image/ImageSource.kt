package io.tuttut.domain.model.image

import java.io.File

sealed interface ImageSource {
    data class Local(val file: File) : ImageSource

    data class Remote(val name: String, val url: String) : ImageSource
}