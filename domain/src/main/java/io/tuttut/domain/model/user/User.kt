package io.tuttut.domain.model.user

import io.tuttut.domain.model.image.ImageSource

data class User(
    val id: String,
    val gardenId: String,
    val name: String,
    val profile: ImageSource.Remote,
) {
    companion object {
        val WITHDREW = User(
            id = "unknown-id",
            gardenId = "unknown-garden-id",
            name = "탈퇴한 유저",
            profile = ImageSource.Remote(
                name = "",
                url = "https://www.tenforums.com/attachments/user-accounts-family-safety/322690d1615743307-user-account-image-log-user.png"
            )
        )
    }
}
