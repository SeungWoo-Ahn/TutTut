package io.tuttut.domain.model.garden

import io.tuttut.domain.model.user.User

data class GardenWithMember(
    val code: String,
    val name: String,
    val memberList: List<User>
)
