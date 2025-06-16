package io.tuttut.presentation.mapper

import io.tuttut.domain.model.crops.Crops
import io.tuttut.presentation.model.crops.MainCropsUiModel

fun Crops.toMainCropsUiModel(): MainCropsUiModel =
    MainCropsUiModel(
        id = id,
        imageUrl = imageUrl,
        name = name,
        nickName = nickName,
        isHarvested = isHarvested,
        wateringDDay = wateringInterval?.let { format(DateFormatStrategy.DDay(lastWatered, it)) } ?: "-",
        growingDDay = growingDay?.let { format(DateFormatStrategy.DDay(plantingDate, it)) } ?: "-",
        diaryCnt = "$diaryCnt 개",
    )