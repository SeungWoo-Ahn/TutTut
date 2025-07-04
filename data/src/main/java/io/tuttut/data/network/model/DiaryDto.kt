package io.tuttut.data.network.model

data class DiaryDto(
    val id: String = "",
    val cropsId: String = "",
    val authorId: String = "",
    val content: String = "",
    val created: String = "",
    val commentCnt: Int = 0,
    val imgUrlList: List<StorageImage> = emptyList(),
)
