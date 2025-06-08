package io.tuttut.domain.model.diary

data class DeleteDiaryRequest(
    val id: String,
    val gardenId: String,
    val cropsId: String,
)
