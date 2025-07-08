package io.tuttut.domain.usecase.image

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.image.SaveLocation
import io.tuttut.domain.repository.ImageRepository
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(image: ImageSource.Remote, location: SaveLocation) {
        imageRepository.deleteImage(image, location)
    }
}