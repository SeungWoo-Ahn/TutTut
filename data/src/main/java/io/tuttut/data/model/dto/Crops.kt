package io.tuttut.data.model.dto

data class Crops(
    val id: String,
    val type: String,
    val name: String,
    val nickName: String,
    val imageUrl: String,
    val wateringGap: Int,
    val growingDay: Int,
    val lastWatered: String,
    val plantingDay: String,
    val diaryCnt: Int,
    val isHarvested: Boolean
)
