package io.tuttut.data.network.model

data class CropsDto(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val nickName: String = "",
    val lastWatered: String = "",
    val plantingDate: String = "",
    val wateringInterval: Int? = null,
    val growingDay: Int? = null,
    val diaryCnt: Int = 0,
    val harvestCnt: Int = 0,
    @field:JvmField
    val isHarvested: Boolean = false,
    val needAlarm: Boolean = false,
    val mainImg: StorageImage? = null,
)

