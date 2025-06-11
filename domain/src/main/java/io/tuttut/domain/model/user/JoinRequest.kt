package io.tuttut.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class JoinRequest(
    val id: String,
    val name: String,
    val imageUrl: String?,
)
