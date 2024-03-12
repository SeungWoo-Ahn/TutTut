package io.tuttut.data.model.dto

data class CropsInfo(
    val key: String,
    val name: String,
    val imageUrl: String,
    val plantingInterval: String,
    val wateringIntervalStr: String,
    val wateringInterval: Int?,
    val growingDay: Int,
    val difficulty: Difficulty,
    val plantingSeasons: List<Season>,
    val harvestSeasons: List<Season>
)

data class Season(
    val start: Int,
    val end: Int
) {
    private fun Int.toStartSeasonStr(): String {
        return if (this % 2 == 0) "${this / 2}월 중순"
        else "${this / 2 + 1}월"
    }

    private fun Int.toEndSeasonStr(): String {
        return if (this % 2 == 0) "${this / 2}월"
        else "${this / 2 + 1}월 중순"
    }

    override fun toString(): String {
        return if (start.toMonth() == end.toMonth()) start.toStartSeasonStr()
        else "${start.toStartSeasonStr()} ~ ${end.toEndSeasonStr()}"
    }
}

private fun Int.toMonth(): Int {
    return if (this % 2 == 0)  this / 2
    else this / 2 + 1
}

fun Season.isRecommended(currentMonth: Int): Boolean {
    return this.start.toMonth() == currentMonth || this.end.toMonth() == currentMonth
}

enum class Difficulty(val displayName: String) {
    EASY("하"),
    MEDIUM("중"),
    DIFFICULT("상")
}