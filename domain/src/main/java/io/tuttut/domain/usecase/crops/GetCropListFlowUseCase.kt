package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCropListFlowUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(isHarvest: Boolean): Flow<List<Crops>> = flow {
        preferenceRepository.getGardenIdFlow().first()?.let { gardenId ->
            cropsRepository.getCropsListFlow(gardenId, isHarvest).collect { cropsList ->
                emit(cropsList)
            }
        }
    }.flowOn(Dispatchers.IO)
}