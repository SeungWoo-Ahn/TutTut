package io.tuttut.data.model.dto

data class Crops(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val nickName: String = "",
    val lastWatered: String = "",
    val plantingDay: String = "",
    val wateringInterval: Int? = null,
    val growingDay: Int = 0,
    val diaryCnt: Int = 0,
    val harvestCnt: Int = 0,
    val isHarvested: Boolean = false,
    val needAlarm: Boolean = false
)
