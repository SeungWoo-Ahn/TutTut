package io.tuttut.data.network.model

import com.google.firebase.firestore.DocumentId

data class DiaryDto(
    @DocumentId
    val id: String = "",
    val cropsId: String = "",
    val authorId: String = "",
    val content: String = "",
    val created: String = "",
    val commentCnt: Int = 0,
    val imgUrlList: List<StorageImage> = emptyList(),
)
