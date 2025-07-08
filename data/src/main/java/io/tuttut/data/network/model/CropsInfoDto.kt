package io.tuttut.data.network.model

data class CropsInfoDto(
    val key: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val plantingInterval: String = "",
    val wateringIntervalStr: String = "",
    val wateringInterval: Int? = null,
    val growingDay: Int = 0,
    val difficulty: String = "",
    val plantingSeasons: List<SeasonDto> = listOf(),
    val harvestSeasons: List<SeasonDto> = listOf(),
)

data class SeasonDto(
    val start: Int = 0,
    val end: Int = 0,
)


