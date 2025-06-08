package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource

data class AddDiaryRequest(
    val authorId: String,
    val gardenId: String,
    val cropsId: String,
    val content: String,
    val imageList: List<ImageSource.Remote>
)
