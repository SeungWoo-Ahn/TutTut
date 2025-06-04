package io.tuttut.domain.model.garden

data class CreateGardenRequest(
    val userId: String,
    val gardenName: String,
    val created: String,
)
