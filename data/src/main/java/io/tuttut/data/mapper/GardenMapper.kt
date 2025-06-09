package io.tuttut.data.mapper

import io.tuttut.data.network.model.GardenDto
import io.tuttut.domain.model.garden.Garden

fun GardenDto.toDomain(): Garden =
    Garden(
        id = id,
        code = code,
        name = name,
        groupIdList = groupIdList
    )
