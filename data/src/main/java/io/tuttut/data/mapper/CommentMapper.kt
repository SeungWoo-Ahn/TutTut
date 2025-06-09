package io.tuttut.data.mapper

import io.tuttut.data.network.model.CommentDto
import io.tuttut.domain.model.comment.Comment

fun CommentDto.toDomain(): Comment =
    Comment(
        id = id,
        authorId = authorId,
        content = content,
        created = created
    )