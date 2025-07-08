package io.tuttut.domain.model.comment

import io.tuttut.domain.model.user.Credential

data class AddCommentRequest(
    val credential: Credential,
    val diaryId: String,
    val content: String,
)
