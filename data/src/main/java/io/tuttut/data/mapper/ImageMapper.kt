package io.tuttut.data.mapper

import io.tuttut.data.network.model.StorageImage
import io.tuttut.domain.model.image.ImageSource

fun StorageImage.toDomain(): ImageSource.Remote =
    ImageSource.Remote(
        name = name,
        url = url
    )

fun ImageSource.Remote.toDto(): StorageImage =
    StorageImage(
        name = name,
        url = url
    )
