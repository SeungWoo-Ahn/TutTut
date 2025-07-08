package io.tuttut.presentation.mapper

import io.tuttut.domain.model.diary.DiaryWithAuthor
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.presentation.model.DetailDiaryUiModel
import io.tuttut.presentation.model.DiaryListItemUiModel
import io.tuttut.presentation.model.DiaryUiModel
import io.tuttut.presentation.model.UserUiModel

private const val DEFAULT_DIARY_IMAGE = "https://www.dementianews.co.kr/news/photo/202104/3708_7612_026.jpg"

fun DiaryWithAuthor.toDetailUiModel(): DetailDiaryUiModel =
    DetailDiaryUiModel(
        id = id,
        firstImageUrl = if (imageList.isNotEmpty()) {
            imageList.first().url
        } else {
            DEFAULT_DIARY_IMAGE
        },
        content = content,
        authorName = author?.name ?: UserUiModel.WITHDREW.name,
    )

fun DiaryWithAuthor.toListItemUiModel(): DiaryListItemUiModel =
    DiaryListItemUiModel(
        id = id,
        isMine = isMine,
        firstImageUrl = if (imageList.isNotEmpty()) {
            imageList.first().url
        } else {
            DEFAULT_DIARY_IMAGE
        },
        content = content,
        authorNameAndDate = "${author?.name ?: UserUiModel.WITHDREW.name} · ${format(DateFormatStrategy.RelativeTime(created))}",
        commentCnt = commentCnt.toString(),
    )

fun DiaryWithAuthor.toUiModel(): DiaryUiModel =
    DiaryUiModel(
        id = id,
        author = author?.toUiModel() ?: UserUiModel.WITHDREW,
        isMine = isMine || author == null,
        content = content,
        commentCnt = "댓글$commentCnt",
        created = format(DateFormatStrategy.RelativeTime(created)),
        imageUrlList = imageList.map(ImageSource.Remote::url).ifEmpty { listOf(DEFAULT_DIARY_IMAGE) }
    )