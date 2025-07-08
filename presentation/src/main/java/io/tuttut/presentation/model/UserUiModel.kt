package io.tuttut.presentation.model

data class UserUiModel(
    val id: String,
    val name: String,
    val profile: String,
) {
    companion object {
        val WITHDREW = UserUiModel(
            id = "withdrew-user-id",
            name = "탈퇴한 유저",
            profile = "https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png"
        )
    }
}

