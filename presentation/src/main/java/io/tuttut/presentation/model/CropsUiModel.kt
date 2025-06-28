package io.tuttut.presentation.model

import io.tuttut.domain.model.cropsInfo.CropsKey

data class MainCropsUiModel(
    val id: String,
    val imageUrl: String,
    val name: String,
    val nickName: String,
    val isHarvested: Boolean,
    val wateringDDay: String,
    val growingDDay: String,
    val diaryCnt: String,
)

data class DetailCropsUiModel(
    val id: String,
    val key: CropsKey,
    val wateringState: WateringState,
    val name: String,
    val nickName: String,
    val imageUrl: String,
    val mainImageUrl: String,
    val plantingDay: String,
    val wateringDay: String,
    val harvestDay: String,
    val lastWateredDay: String,
    val wateringInterval: String,
    val growingDay: String,
    val harvest: String,
    val isHarvested: Boolean,
)

enum class WateringState {
    IMPOSSIBLE, POSSIBLE, WATERED_TODAY
}