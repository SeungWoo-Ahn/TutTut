package io.tuttut.data.mapper

import io.tuttut.data.network.constant.CUSTOM_IMAGE
import io.tuttut.data.network.model.CropsDto
import io.tuttut.data.util.DateProvider
import io.tuttut.domain.model.crops.AddCropsRequest
import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.model.crops.UpdateCropsRequest
import io.tuttut.domain.model.cropsInfo.CropsKey

fun CropsDto.toDomain(): Crops =
    Crops(
        id = id,
        key = CropsKey.fromKey(key),
        imageUrl = CUSTOM_IMAGE,
        name = name,
        nickName = nickName,
        lastWatered = lastWatered,
        plantingDate = plantingDate,
        wateringInterval = wateringInterval,
        growingDay = growingDay,
        diaryCnt = diaryCnt,
        harvest = harvestCnt,
        isHarvested = isHarvested,
        needAlarm = needAlarm,
        mainImage = mainImg?.toDomain()
    )

fun AddCropsRequest.toDto(id: String): CropsDto =
    CropsDto(
        id = id,
        key = key.key,
        name = name,
        nickName = nickName,
        lastWatered = DateProvider.now(),
        plantingDate = plantingDate,
        wateringInterval = wateringInterval,
        growingDay = growingDay,
    )

fun UpdateCropsRequest.toUpdateMap(): Map<String, Any?> =
    mapOf(
        "name" to name,
        "nickName" to nickName,
        "plantingDate" to plantingDate,
        "wateringInterval" to wateringInterval,
        "growingDay" to growingDay,
    )