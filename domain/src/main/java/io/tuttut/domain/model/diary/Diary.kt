package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.user.User

data class Diary(
    val id: String,
    val cropsId: String,
    val author: User,
    val content: String,
    val created: String,
    val commentCnt: Int,
    val imageList: List<ImageSource.Remote>
)