package io.tuttut.data.model.dto

data class Diary(
    val id: String = "",
    val cropsId: String = "",
    val authorId: String = ""
)

fun Diary.toMap(): HashMap<String, Any> = hashMapOf(
    TODO("필드 채우기")
)
