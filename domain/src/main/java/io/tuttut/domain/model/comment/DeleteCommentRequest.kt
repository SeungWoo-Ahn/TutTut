package io.tuttut.domain.model.comment

data class DeleteCommentRequest(
    val gardenId: String,
    val diaryId: String,
    val commentId: String,
)