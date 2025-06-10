package io.tuttut.data.mapper

import com.google.firebase.firestore.FieldValue
import io.tuttut.data.network.constant.FirebaseKey
import io.tuttut.data.network.model.DiaryDto
import io.tuttut.data.network.model.StorageImage
import io.tuttut.data.util.DateProvider
import io.tuttut.domain.model.diary.AddDiaryRequest
import io.tuttut.domain.model.diary.DeleteDiaryRequest
import io.tuttut.domain.model.diary.Diary
import io.tuttut.domain.model.diary.UpdateDiaryRequest
import io.tuttut.domain.model.image.ImageSource

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

fun AddDiaryRequest.toDto(id: String): DiaryDto =
    DiaryDto(
        id = id,
        cropsId = cropsId,
        authorId = credential.userId,
        content = content,
        created = DateProvider.now(),
        imgUrlList = imageList.map(ImageSource.Remote::toDto)
    )

fun AddDiaryRequest.toUpdateMap(): Map<String, Any?> {
    val updateMap = mutableMapOf<String, Any?>()
    updateMap[FirebaseKey.CROPS_DIARY_COUNT] = FieldValue.increment(1)
    if (imageList.isNotEmpty()) {
        updateMap[FirebaseKey.CROPS_MAIN_IMAGE] = imageList[0].toDto()
    }
    return updateMap
}

fun UpdateDiaryRequest.toUpdateMap(): Map<String, Any?> =
    mapOf(
        "content" to content,
        "imgUrlList" to imageList.map(ImageSource.Remote::toDto)
    )

fun DeleteDiaryRequest.toUpdateMap(): Map<String, Any?> =
    mapOf(
        FirebaseKey.CROPS_DIARY_COUNT to FieldValue.increment(-1)
    )