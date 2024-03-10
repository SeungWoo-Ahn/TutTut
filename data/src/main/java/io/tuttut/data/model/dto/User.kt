package io.tuttut.data.model.dto

data class User(
    val id: String,
    val name: String,
    val profileUrl: String?,
    val gardenId: String,
)