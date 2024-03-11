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
) {
    override fun toString(): String {
        fun Int.toSeasonStr(): String {
            return if (this % 2 == 0) "${this}월 중순"
            else "${this}월"
        }
        return "${start.toSeasonStr()} ~ ${end.toSeasonStr()}"
    }
}

enum class Difficulty(val displayName: String) {
    EASY("하"),
    MEDIUM("중"),
    DIFFICULT("상")
}