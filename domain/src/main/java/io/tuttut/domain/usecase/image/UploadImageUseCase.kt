package io.tuttut.domain.usecase.image

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.image.SaveLocation
import io.tuttut.domain.repository.ImageRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(
        imageSource: ImageSource,
        location: SaveLocation
    ): Result<ImageSource.Remote> = runCatching {
        when (imageSource) {
            is ImageSource.Local -> {
                val file = imageSource.file
                val url = imageRepository.uploadImage(file, location)
                ImageSource.Remote(file.name, url)
            }
            is ImageSource.Remote -> imageSource
        }
    }
}