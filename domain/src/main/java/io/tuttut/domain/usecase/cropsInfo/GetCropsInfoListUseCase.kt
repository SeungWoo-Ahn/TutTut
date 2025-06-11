package io.tuttut.domain.usecase.cropsInfo

import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.repository.CropsInfoRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import javax.inject.Inject

class GetCropsInfoListUseCase @Inject constructor(
    private val cropsInfoRepository: CropsInfoRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(): Result<List<CropsInfo>> = runCatchingExceptCancel {
        val cachedCropsInfoList = preferenceRepository.getCropsInfoList()
        if (cachedCropsInfoList.isNotEmpty()) {
            return@runCatchingExceptCancel cachedCropsInfoList
        }
        cropsInfoRepository.getCropsInfoList()
            .also { cropsInfoList -> preferenceRepository.setCropsInfoList(cropsInfoList) }
    }
}