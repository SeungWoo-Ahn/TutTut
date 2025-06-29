package io.tuttut.presentation.mapper

import io.tuttut.domain.model.diary.DiaryWithAuthor
import io.tuttut.presentation.model.DetailDiaryUiModel
import io.tuttut.presentation.model.DiaryListItemUiModel

fun DiaryWithAuthor.toDetailDiaryUiModel(): DetailDiaryUiModel =
    DetailDiaryUiModel(
        id = id,
        firstImageUrl = if (imageList.isNotEmpty()) {
            imageList.first().url
        } else {
            "https://www.dementianews.co.kr/news/photo/202104/3708_7612_026.jpg"
        },
        content = content,
        authorName = author?.name ?: "탈퇴한 유저",
    )

fun DiaryWithAuthor.toDiaryListItemUiModel(): DiaryListItemUiModel =
    DiaryListItemUiModel(
        id = id,
        isMine = isMine,
        firstImageUrl = if (imageList.isNotEmpty()) {
            imageList.first().url
        } else {
            "https://www.dementianews.co.kr/news/photo/202104/3708_7612_026.jpg"
        },
        content = content,
        authorNameAndDate = "${author?.name ?: "탈퇴한 유저"} · ${format(DateFormatStrategy.RelativeTime(created))}",
        commentCnt = commentCnt.toString(),
    )