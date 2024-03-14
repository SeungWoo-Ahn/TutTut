package io.tuttut.data.model.dto

data class CropsInfo(
    val key: String = CUSTOM_KEY,
    val name: String = CUSTOM_NAME,
    val imageUrl: String = CUSTOM_IMAGE,
    val plantingInterval: String = "",
    val wateringIntervalStr: String = "",
    val wateringInterval: Int? = null,
    val growingDay: Int = 0,
    val difficulty: Difficulty = Difficulty.EASY,
    val plantingSeasons: List<Season> = listOf(),
    val harvestSeasons: List<Season> = listOf(),
)

data class Season(
    val start: Int,
    val end: Int,
) {
    constructor() : this(0, 0) // fireStore deserialized 용
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

const val CUSTOM_KEY = "custom"
const val CUSTOM_NAME = "새로운 작물"
const val CUSTOM_IMAGE = "https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fy62O7%2Fbtq2noYOJK9%2FYlgUrz5JDxc6Keh42CQHoK%2Fimg.png"