package io.tuttut.domain.model.garden

data class Garden(
    val id: String,
    val code: String,
    val name: String,
    val groupIdList: List<String>,
)
