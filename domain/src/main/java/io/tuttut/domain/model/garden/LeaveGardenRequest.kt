package io.tuttut.domain.model.garden

data class LeaveGardenRequest(
    val userId: String,
    val gardenId: String,
)
