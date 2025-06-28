package io.tuttut.presentation.mapper

import io.tuttut.domain.model.crops.Crops
import io.tuttut.presentation.model.DetailCropsUiModel
import io.tuttut.presentation.model.MainCropsUiModel
import io.tuttut.presentation.model.WateringState

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

fun Crops.toDetailCropsUiModel(): DetailCropsUiModel =
    DetailCropsUiModel(
        id = id,
        key = key,
        wateringState = Pair(wateringInterval, lastWatered).toWateringState(),
        name = name,
        nickName = nickName,
        imageUrl = imageUrl,
        mainImageUrl = mainImage.url,
        plantingDay = format(DateFormatStrategy.PlantingDay(plantingDate)),
        wateringDay = wateringInterval?.let { format(DateFormatStrategy.WateringDay(lastWatered, it)) } ?: "-",
        harvestDay = growingDay?.let { format(DateFormatStrategy.HarvestDay(plantingDate, it)) } ?: "-",
        lastWateredDay = format(DateFormatStrategy.LastWateredDay(lastWatered)),
        wateringInterval = wateringInterval?.let { "${it}일" } ?: "-",
        growingDay = growingDay?.let { "${it}일" } ?: "-",
        harvest = "$harvest 번",
        isHarvested = isHarvested,
    )

private fun Pair<Int?, String>.toWateringState(): WateringState =
    when {
        first == null -> WateringState.IMPOSSIBLE
        second == format(DateFormatStrategy.Today) -> WateringState.WATERED_TODAY
        else -> WateringState.POSSIBLE
    }