package io.tuttut.presentation.model

data class CropsInfoUiModel(
    val name: String,
    val difficulty: String,
    val plantingSeasons: String,
    val harvestSeasons: String,
    val plantingInterval: String,
    val wateringInterval: String,
)