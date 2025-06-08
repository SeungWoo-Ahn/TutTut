package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource

data class UpdateDiaryRequest(
    val id: String,
    val gardenId: String,
    val content: String,
    val imageList: List<ImageSource.Remote>,
)
