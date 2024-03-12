package io.tuttut.data.repository

import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Response

interface CropsInfoRepository {

    suspend fun addCropsInfoByAdmin(cropsInfo: CropsInfo): Response<Boolean>
}