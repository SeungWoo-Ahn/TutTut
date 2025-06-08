package io.tuttut.domain.model.crops

import io.tuttut.domain.model.cropsInfo.CropsKey

data class PutCropsRequest(
    val key: CropsKey,
    val name: String,
    val nickName: String,
    val plantingDate: String,
    val wateringInterval: Int?,
    val growingDay: Int?,
)
