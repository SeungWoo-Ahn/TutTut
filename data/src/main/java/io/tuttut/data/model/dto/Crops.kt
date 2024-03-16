package io.tuttut.data.model.dto

data class Crops(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val nickName: String = "",
    val lastWatered: String = "",
    val plantingDate: String = "",
    val mainImgUrl: String? = null,
    val wateringInterval: Int? = null,
    val growingDay: Int? = null,
    val diaryCnt: Int = 0,
    val harvestCnt: Int = 0,
    @field:JvmField
    val isHarvested: Boolean = false,
    val needAlarm: Boolean = false
)

fun Crops.toMap(): HashMap<String, Any?> = hashMapOf(
    "name" to name,
    "nickName" to nickName,
    "lastWatered" to lastWatered,
    "plantingDate" to plantingDate,
    "wateringInterval" to wateringInterval,
    "growingDay" to growingDay,
    "diaryCnt" to diaryCnt,
    "harvestCnt" to harvestCnt,
    "isHarvested" to isHarvested,
    "needAlarm" to needAlarm
)

const val DEFAULT_MAIN_IMAGE = "https://www.dementianews.co.kr/news/photo/202104/3708_7612_026.jpg"