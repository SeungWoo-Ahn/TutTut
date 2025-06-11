package io.tuttut.domain.model.comment

data class Comment(
    val id: String,
    val authorId: String,
    val created: String,
    val content: String,
)
