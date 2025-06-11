package io.tuttut.domain.model.crops

import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.image.ImageSource

data class Crops(
    val id: String,
    val key: CropsKey,
    val name: String,
    val nickName: String,
    val lastWatered: String,
    val plantingDate: String,
    val wateringInterval: Int?,
    val growingDay: Int?,
    val diaryCnt: Int,
    val harvest: Int,
    val isHarvested: Boolean,
    val needAlarm: Boolean,
    val mainImage: ImageSource.Remote?
)
