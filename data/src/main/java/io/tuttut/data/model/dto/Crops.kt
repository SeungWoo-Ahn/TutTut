package io.tuttut.data.model.dto

data class Crops(
    val id: String,
    val key: String,
    val nickName: String,
    val lastWatered: String,
    val plantingDay: String,
    val wateringInterval: Int,
    val growingDay: Int,
    val diaryCnt: Int,
    val harvestCnt: Int = 0,
    val isHarvested: Boolean,
    val needAlarm: Boolean = false
)
