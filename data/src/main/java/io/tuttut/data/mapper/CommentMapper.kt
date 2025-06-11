package io.tuttut.data.mapper

import io.tuttut.data.network.model.CommentDto
import io.tuttut.data.util.DateProvider
import io.tuttut.domain.model.comment.AddCommentRequest
import io.tuttut.domain.model.comment.Comment

fun CommentDto.toDomain(): Comment =
    Comment(
        id = id,
        authorId = authorId,
        content = content,
        created = created
    )

fun AddCommentRequest.toDto(id: String): CommentDto =
    CommentDto(
        id = id,
        authorId = credential.userId,
        content = content,
        created = DateProvider.now()
    )