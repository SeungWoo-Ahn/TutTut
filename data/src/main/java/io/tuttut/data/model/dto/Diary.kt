package io.tuttut.data.model.dto

data class Diary(
    val id: String = "",
    val cropsId: String = "",
    val authorId: String = "",
    val content: String = "",
    val created: String = "",
    val commentCnt: Int = 0,
    val imgUrlList: List<StorageImage> = emptyList(),
)

fun Diary.toMap(): HashMap<String, Any> = hashMapOf(
    TODO("필드 채우기")
)
