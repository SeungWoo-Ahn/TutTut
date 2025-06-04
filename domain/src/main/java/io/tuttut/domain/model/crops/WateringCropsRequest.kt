package io.tuttut.domain.model.crops

data class WateringCropsRequest(
    val gardenId: String,
    val cropsId: String,
    val today: String,
)
