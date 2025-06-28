package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
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
    private val mapCropsImageUrlByKeyUseCase: MapCropsImageUrlByKeyUseCase,
) {
    operator fun invoke(isHarvest: Boolean): Flow<List<Crops>> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                cropsRepository.getCropsListFlow(credential.gardenId, isHarvest)
            }
            .map { cropsList ->
                cropsList.map { crops ->
                    mapCropsImageUrlByKeyUseCase(crops)
                }
            }
            .catch {
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
}