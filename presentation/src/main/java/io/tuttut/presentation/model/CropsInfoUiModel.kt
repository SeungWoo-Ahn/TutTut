package io.tuttut.presentation.model

import io.tuttut.domain.model.cropsInfo.CropsKey

data class CropsInfoUiModel(
    val name: String,
    val key: CropsKey,
    val imageUrl: String,
    val difficulty: String,
    val plantingSeasons: String,
    val harvestSeasons: String,
    val plantingInterval: String,
    val wateringInterval: String,
)