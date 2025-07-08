package io.tuttut.data.network.model

data class GardenDto(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val created: String = "",
    val groupIdList: List<String> = listOf(),
)
