package io.tuttut.domain.model.garden

data class JoinGardenRequest(
    val userId: String,
    val gardenId: String,
)