package io.tuttut.domain.repository

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.image.SaveLocation
import java.io.File

interface ImageRepository {
    suspend fun uploadImage(file: File, location: SaveLocation): String

    suspend fun deleteImage(image: ImageSource.Remote, location: SaveLocation)
}