package io.tuttut.data.model.dto

data class Comment(
    val id: String = "",
    val authorId: String = "",
    val created: String = "",
    val content: String = ""
)

fun Comment.toMap(): HashMap<String, Any> = hashMapOf(
    TODO("필드 채우기")
)
