package io.tuttut.data.model

data class User(
    val id: String,
    val name: String,
    val profileUrl: String?,
    val gardenId: String,
    val cropsIdList: List<String>
)
