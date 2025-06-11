package io.tuttut.domain.model.diary

import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.user.Credential

data class AddDiaryRequest(
    val credential: Credential,
    val cropsId: String,
    val content: String,
    val imageList: List<ImageSource.Remote>
)
