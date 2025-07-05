package io.tuttut.presentation.mapper

import io.tuttut.domain.model.crops.Crops
import io.tuttut.presentation.model.CropsInfoItemUiModel
import io.tuttut.presentation.model.DetailCropsUiModel
import io.tuttut.presentation.model.MainCropsUiModel
import io.tuttut.presentation.model.WateringState

private const val DEFAULT_MAIN_IMAGE = "https://www.dementianews.co.kr/news/photo/202104/3708_7612_026.jpg"

fun Crops.toMainCropsUiModel(): MainCropsUiModel =
    MainCropsUiModel(
        id = id,
        imageUrl = imageUrl ?: CropsInfoItemUiModel.CUSTOM.imageUrl,
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
        imageUrl = imageUrl ?: CropsInfoItemUiModel.CUSTOM.imageUrl,
        mainImageUrl = mainImage?.url ?: DEFAULT_MAIN_IMAGE,
        plantingDay = format(DateFormatStrategy.PlantingDay(plantingDate)),
        wateringDay = wateringInterval?.let { format(DateFormatStrategy.WateringDay(lastWatered, it)) } ?: "-",
        harvestDay = growingDay?.let { format(DateFormatStrategy.HarvestDay(plantingDate, it)) } ?: "-",
        lastWateredDay = wateringInterval?.let { format(DateFormatStrategy.LastWateredDay(lastWatered)) } ?: "-" ,
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

fun Crops.toItemUiModel(): CropsInfoItemUiModel =
    CropsInfoItemUiModel(
        name = name,
        key = key,
        imageUrl = imageUrl ?: CropsInfoItemUiModel.CUSTOM.imageUrl,
        growingDay = growingDay ?: 0
    )