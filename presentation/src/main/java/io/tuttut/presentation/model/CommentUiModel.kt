package io.tuttut.presentation.model

data class CommentUiModel(
    val id: String,
    val author: UserUiModel,
    val isMine: Boolean,
    val content: String,
    val created: String,
)