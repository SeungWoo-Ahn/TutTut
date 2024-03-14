package io.tuttut.data.mapper

import io.tuttut.data.model.dto.Garden

fun Map<String, Any>.mapToGarden(): Garden {
    val id = this["id"] as String
    val code = this["code"] as String
    val name = this["name"] as String
    val created = this["created"] as String
    val groupIdList = this["groupIdList"] as List<String>
    return Garden(id, code, name, created, groupIdList)
}