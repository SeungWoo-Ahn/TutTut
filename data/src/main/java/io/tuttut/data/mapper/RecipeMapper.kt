package io.tuttut.data.mapper

import io.tuttut.data.network.model.RecipeDto
import io.tuttut.domain.model.cropsInfo.Recipe

fun RecipeDto.toDomain(): Recipe =
    Recipe(
        title = title,
        imageUrl = imgUrl,
        link = link
    )