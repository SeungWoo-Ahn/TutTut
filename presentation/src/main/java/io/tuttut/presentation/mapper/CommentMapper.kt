package io.tuttut.presentation.mapper

import io.tuttut.domain.model.comment.CommentWithAuthor
import io.tuttut.presentation.model.CommentUiModel
import io.tuttut.presentation.model.UserUiModel

fun CommentWithAuthor.toUiModel(): CommentUiModel =
    CommentUiModel(
        id = id,
        author = author?.toUiModel() ?: UserUiModel.WITHDREW,
        isMine = isMine,
        content = content,
        created = format(DateFormatStrategy.RelativeTime(created))
    )