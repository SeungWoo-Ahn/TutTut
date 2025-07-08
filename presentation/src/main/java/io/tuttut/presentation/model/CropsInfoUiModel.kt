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

data class CropsInfoItemUiModel(
    val name: String,
    val key: CropsKey,
    val imageUrl: String,
    val growingDay: Int,
) {
    companion object {
        val CUSTOM = CropsInfoItemUiModel(
            name = "새로운 작물",
            key = CropsKey.CUSTOM,
            imageUrl = "https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fy62O7%2Fbtq2noYOJK9%2FYlgUrz5JDxc6Keh42CQHoK%2Fimg.png",
            growingDay = 0,
        )
    }
}