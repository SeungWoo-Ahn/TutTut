package io.tuttut.data.mapper

import io.tuttut.data.network.model.DiaryDto
import io.tuttut.data.network.model.StorageImage
import io.tuttut.domain.model.diary.Diary

fun DiaryDto.toDomain(): Diary =
    Diary(
        id = id,
        cropsId = cropsId,
        authorId = authorId,
        content = content,
        created = created,
        commentCnt = commentCnt,
        imageList = imgUrlList.map(StorageImage::toDomain)
    )