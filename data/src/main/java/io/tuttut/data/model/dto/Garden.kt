package io.tuttut.data.model.dto

data class Garden(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val created: String = "",
    val groupIdList: List<String> = listOf(),
)
