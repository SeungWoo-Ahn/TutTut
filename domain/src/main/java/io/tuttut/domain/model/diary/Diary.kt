package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource

data class Diary(
    val id: String,
    val cropsId: String,
    val authorId: String,
    val content: String,
    val created: String,
    val commentCnt: Int,
    val imageList: List<ImageSource.Remote>
)