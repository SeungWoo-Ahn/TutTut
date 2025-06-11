package io.tuttut.domain.model.cropsInfo

data class CropsInfo(
    val key: CropsKey,
    val name: String,
    val imageUrl: String,
    val plantingInterval: String,
    val wateringIntervalStr: String,
    val wateringInterval: Int?,
    val growingDay: Int,
    val difficulty: Difficulty,
    val plantingSeasons: List<Season>,
    val harvestSeasons: List<Season>,
)
