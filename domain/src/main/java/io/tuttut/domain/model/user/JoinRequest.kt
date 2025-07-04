package io.tuttut.domain.model.user

data class JoinRequest(
    val id: String,
    val name: String,
    val imageUrl: String?,
)
