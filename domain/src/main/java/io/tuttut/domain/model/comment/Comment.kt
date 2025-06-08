package io.tuttut.domain.model.comment

import io.tuttut.domain.model.user.User

data class Comment(
    val id: String,
    val author: User,
    val created: String,
    val content: String,
)
