package io.tuttut.data.network.model

import com.google.firebase.firestore.DocumentId

data class CommentDto(
    @DocumentId
    val id: String = "",
    val authorId: String = "",
    val created: String = "",
    val content: String = ""
)