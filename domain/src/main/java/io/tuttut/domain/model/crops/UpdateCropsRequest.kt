package io.tuttut.domain.model.crops

data class UpdateCropsRequest(
    val id: String,
    val name: String,
    val nickName: String,
    val plantingDate: String,
    val wateringInterval: Int?,
    val growingDay: Int?,
)
