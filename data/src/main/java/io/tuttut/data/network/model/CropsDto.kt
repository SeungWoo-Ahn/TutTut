package io.tuttut.data.network.model

import com.google.firebase.firestore.DocumentId

data class Crops(
    @DocumentId
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

fun Crops.toMap(): HashMap<String, Any?> = hashMapOf(
    "name" to name,
    "nickName" to nickName,
    "lastWatered" to lastWatered,
    "plantingDate" to plantingDate,
    "wateringInterval" to wateringInterval,
    "growingDay" to growingDay,
    "needAlarm" to needAlarm
)

