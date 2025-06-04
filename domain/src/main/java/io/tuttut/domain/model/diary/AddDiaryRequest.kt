package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource

data class AddDiaryRequest(
    val gardenId: String,
    val cropsId: String,
    val authorId: String,
    val content: String,
    val created: String,
    val imageList: List<ImageSource.Local>
)
