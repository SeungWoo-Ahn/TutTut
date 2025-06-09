package io.tuttut.data.mapper

import io.tuttut.data.network.model.CropsDto
import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.model.cropsInfo.CropsKey

fun CropsDto.toDomain(): Crops =
    Crops(
        id = id,
        key = CropsKey.fromKey(key),
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