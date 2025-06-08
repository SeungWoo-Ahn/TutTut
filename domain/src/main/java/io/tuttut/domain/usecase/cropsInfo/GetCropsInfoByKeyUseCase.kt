package io.tuttut.domain.usecase.cropsInfo

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.repository.CropsInfoRepository
import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetCropsInfoByKeyUseCase @Inject constructor(
    private val cropsInfoRepository: CropsInfoRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(cropsKey: CropsKey): Result<CropsInfo> = runCatching {
        val cachedCropsInfo = preferenceRepository.getCropsInfoByKey(cropsKey)
        if (cachedCropsInfo != null) {
            return@runCatching cachedCropsInfo
        }
        cropsInfoRepository.getCropsInfoByKey(cropsKey)
            .getOrThrow()
            .also { cropsInfo -> preferenceRepository.setCropsInfo(cropsInfo) }
    }
}