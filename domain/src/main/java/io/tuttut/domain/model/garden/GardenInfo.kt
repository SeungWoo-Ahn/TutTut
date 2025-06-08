package io.tuttut.domain.model.garden

import io.tuttut.domain.model.user.User

data class GardenInfo(
    val code: String,
    val name: String,
    val userList: List<User>,
)
