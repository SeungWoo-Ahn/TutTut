package io.tuttut.domain.model.comment

data class AddCommentRequest(
    val gardenId: String,
    val diaryId: String,
    val authorId: String,
    val content: String,
    val created: String,
)
