package io.tuttut.data.mapper

import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Difficulty
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.Season

fun Map<String, Any>.mapToGarden(): Garden {
    val id = this["id"] as String
    val code = this["code"] as String
    val name = this["name"] as String
    val created = this["created"] as String
    val groupIdList = this["groupIdList"] as List<String>
    return Garden(id, code, name, created, groupIdList)
}

fun Map<String, Any>.mapToCropsInfo(): CropsInfo {
    val key = this["key"] as String
    val name = this["name"] as String
    val imageUrl = this["imageUrl"] as String
    val plantingInterval = this["plantingInterval"] as String
    val wateringIntervalStr = this["wateringIntervalStr"] as String
    val wateringInterval = this["wateringInterval"] as Int?
    val growingDay = this["growingDay"] as Int
    val difficulty = this["difficulty"] as Difficulty
    val plantingSeasons = (this["plantingSeasons"] as List<*>).map { it as Season }
    val harvestSeasons = (this["harvestSeasons"] as List<*>).map { it as Season }

    return CropsInfo(
        key,
        name,
        imageUrl,
        plantingInterval,
        wateringIntervalStr,
        wateringInterval,
        growingDay,
        difficulty,
        plantingSeasons,
        harvestSeasons
    )
}