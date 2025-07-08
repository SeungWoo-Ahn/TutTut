package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.user.User

data class DiaryWithAuthor(
    val id: String,
    val cropsId: String,
    val author: User?,
    val isMine: Boolean,
    val content: String,
    val created: String,
    val commentCnt: Int,
    val imageList: List<ImageSource.Remote>,
)

fun Diary.withAuthor(author: User?, isMine: Boolean): DiaryWithAuthor =
    DiaryWithAuthor(
        id = id,
        cropsId = cropsId,
        author = author,
        isMine = isMine,
        content = content,
        created = created,
        commentCnt = commentCnt,
        imageList = imageList,
    )
