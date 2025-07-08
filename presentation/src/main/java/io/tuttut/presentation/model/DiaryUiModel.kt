package io.tuttut.presentation.model

data class  DetailDiaryUiModel(
    val id: String,
    val firstImageUrl: String,
    val content: String,
    val authorName: String
)

data class DiaryListItemUiModel(
    val id: String,
    val isMine: Boolean,
    val firstImageUrl: String,
    val content: String,
    val authorNameAndDate: String,
    val commentCnt: String,
)

data class DiaryUiModel(
    val id: String,
    val author: UserUiModel,
    val isMine: Boolean,
    val content: String,
    val commentCnt: String,
    val created: String,
    val imageUrlList: List<String>,
)