package io.tuttut.presentation.model

data class GardenUiModel(
    val name: String,
    val code: String,
    val memberList: List<UserUiModel>,
)