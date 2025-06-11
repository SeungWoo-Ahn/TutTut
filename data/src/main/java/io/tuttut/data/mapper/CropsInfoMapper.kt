package io.tuttut.data.mapper

import io.tuttut.data.network.model.CropsInfoDto
import io.tuttut.data.network.model.SeasonDto
import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.cropsInfo.Difficulty
import io.tuttut.domain.model.cropsInfo.Season

fun SeasonDto.toDomain(): Season =
    Season(
        start = start,
        end = end
    )

fun CropsInfoDto.toDomain(): CropsInfo =
    CropsInfo(
        key = CropsKey.fromKey(key),
        name = name,
        imageUrl = imageUrl,
        plantingInterval = plantingInterval,
        wateringIntervalStr = wateringIntervalStr,
        wateringInterval = wateringInterval,
        growingDay = growingDay,
        difficulty = Difficulty.valueOf(difficulty),
        plantingSeasons = plantingSeasons.map(SeasonDto::toDomain),
        harvestSeasons = harvestSeasons.map(SeasonDto::toDomain)
    )