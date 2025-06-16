package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.cropsInfo.GetCropsInfoByKeyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCropListFlowUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val getCropsInfoByKeyUseCase: GetCropsInfoByKeyUseCase,
) {
    operator fun invoke(isHarvest: Boolean): Flow<List<Crops>> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                cropsRepository.getCropsListFlow(credential.gardenId, isHarvest)
            }
            .map { cropsList ->
                cropsList.map { crops ->
                    when (val key = crops.key) {
                        CropsKey.CUSTOM -> crops
                        else -> {
                            getCropsInfoByKeyUseCase(key)
                                .getOrNull()?.let {
                                    crops.copy(imageUrl = it.imageUrl)
                                } ?: crops
                        }
                    }
                }
            }
            .catch {
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
}