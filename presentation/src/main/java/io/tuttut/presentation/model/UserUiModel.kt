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
            profile = "https://www.tenforums.com/attachments/user-accounts-family-safety/322690d1615743307-user-account-image-log-user.png"
        )
    }
}

