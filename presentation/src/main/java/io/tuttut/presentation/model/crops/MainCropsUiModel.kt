package io.tuttut.presentation.model.crops

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