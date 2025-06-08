package io.tuttut.domain.usecase.cropsInfo

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.repository.CropsInfoRepository
import io.tuttut.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetCropsInfoListUseCase @Inject constructor(
    private val cropsInfoRepository: CropsInfoRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<List<CropsInfo>> = runCatching {
        val cachedCropsInfoList = preferenceRepository.getCropsInfoList()
        if (cachedCropsInfoList.isNotEmpty()) {
            return@runCatching cachedCropsInfoList
        }
        cropsInfoRepository.getCropsInfoList()
            .getOrThrow()
            .also { cropsInfoList -> preferenceRepository.setCropsInfoList(cropsInfoList) }
    }
}