package io.tuttut.data.model.dto

data class CropsInfo(
    val key: String,
    val name: String,
    val imageUrl: String,
    val difficulty: Difficulty,
    val plantingSeasons: List<Season>,
    val plantingInterval: String,
    val wateringInterval: Int,
    val wateringIntervalStr: String,
    val harvestSeasons: List<Season>
)

data class Season(
    val start: Int,
    val end: Int
)

enum class Difficulty(val displayName: String) {
    EASY("하"),
    MEDIUM("중"),
    DIFFICULT("상")
}