package io.tuttut.presentation.mapper

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.Difficulty
import io.tuttut.domain.model.cropsInfo.Season
import io.tuttut.presentation.model.CropsInfoUiModel

fun CropsInfo.toUiModel(): CropsInfoUiModel =
    CropsInfoUiModel(
        name = name,
        key = key,
        imageUrl = imageUrl,
        difficulty = difficulty.toDisplayName(),
        plantingSeasons = plantingSeasons.joinToString("\n", transform = Season::toDisplayName),
        harvestSeasons = harvestSeasons.joinToString("\n", transform = Season::toDisplayName),
        plantingInterval = plantingInterval,
        wateringInterval = wateringIntervalStr,
    )

private fun Difficulty.toDisplayName(): String =
    when (this) {
        Difficulty.EASY -> "쉬움"
        Difficulty.MEDIUM -> "보통"
        Difficulty.DIFFICULT -> "어려움"
    }

private fun Season.toDisplayName(): String {
    fun Int.toMonth(): Int {
        return if (this % 2 == 0) this / 2
        else this / 2 + 1
    }
    fun Int.toStartMonthStr(): String {
        return if (this % 2 == 0) "${this / 2}월 중순"
        else "${this / 2 + 1}월"
    }
    fun Int.toEndMonthStr(): String {
        return if (this % 2 == 0) "${this / 2}월"
        else "${this / 2 + 1}월 중순"
    }
    return if (start.toMonth() == end.toMonth()) {
        start.toStartMonthStr()
    } else {
        "${start.toStartMonthStr()} ~ ${end.toEndMonthStr()}"
    }
}