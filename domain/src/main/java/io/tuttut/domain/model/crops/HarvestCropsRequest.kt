package io.tuttut.domain.model.crops

data class HarvestCropsRequest(
    val gardenId: String,
    val cropsId: String,
    val count: Int,
)
