package io.tuttut.presentation.mapper

import io.tuttut.domain.model.user.User
import io.tuttut.presentation.model.UserUiModel

fun User.toUiModel(): UserUiModel =
    UserUiModel(
        id = id,
        name = name,
        profile = profile.url,
    )