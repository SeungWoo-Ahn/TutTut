package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.user.User

data class DiaryWithAuthor(
    val id: String,
    val author: User,
    val content: String,
    val created: String,
    val commentCnt: Int,
    val imageList: List<ImageSource.Remote>,
)

fun Diary.toDiaryWithAuthor(author: User): DiaryWithAuthor =
    DiaryWithAuthor(
        id = id,
        author = author,
        content = content,
        created = created,
        commentCnt = commentCnt,
        imageList = imageList,
    )
