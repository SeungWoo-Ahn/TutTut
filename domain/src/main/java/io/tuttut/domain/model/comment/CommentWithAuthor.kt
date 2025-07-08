package io.tuttut.domain.model.comment

import io.tuttut.domain.model.user.User

data class CommentWithAuthor(
    val id: String,
    val author: User?,
    val isMine: Boolean,
    val content: String,
    val created: String,
)

fun Comment.withAuthor(author: User?, isMine: Boolean): CommentWithAuthor =
    CommentWithAuthor(
        id = id,
        author = author,
        isMine = isMine,
        content = content,
        created = created,
    )