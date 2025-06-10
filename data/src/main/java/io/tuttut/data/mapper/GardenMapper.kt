package io.tuttut.data.mapper

import io.tuttut.data.network.model.GardenDto
import io.tuttut.data.util.DateProvider
import io.tuttut.domain.model.garden.CreateGardenRequest
import io.tuttut.domain.model.garden.Garden

fun GardenDto.toDomain(): Garden =
    Garden(
        id = id,
        code = code,
        name = name,
        groupIdList = groupIdList
    )

fun CreateGardenRequest.toDto(id: String): GardenDto =
    GardenDto(
        id = id,
        code = id.substring(0, 6),
        name = gardenName,
        created = DateProvider.now(),
        groupIdList = listOf(userId)
    )
