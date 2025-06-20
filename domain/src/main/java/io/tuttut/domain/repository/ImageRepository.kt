package io.tuttut.domain.repository

import io.tuttut.domain.model.image.ImageSource
import java.io.File

interface ImageRepository {
    suspend fun uploadImage(file: File): Result<String>

    suspend fun deleteImage(image: ImageSource.Remote): Result<Unit>
}