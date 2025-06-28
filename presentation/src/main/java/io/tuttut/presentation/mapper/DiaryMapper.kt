package io.tuttut.presentation.mapper

import io.tuttut.domain.model.diary.DiaryWithAuthor
import io.tuttut.presentation.model.DetailDiaryUiModel

fun DiaryWithAuthor.toDetailDiaryUiModel(): DetailDiaryUiModel =
    DetailDiaryUiModel(
        id = id,
        firstImageUrl = if (imageList.isNotEmpty()) {
            imageList.first().url
        } else {
            "https://www.dementianews.co.kr/news/photo/202104/3708_7612_026.jpg"
        },
        content = content,
        authorName = author.name,
    )