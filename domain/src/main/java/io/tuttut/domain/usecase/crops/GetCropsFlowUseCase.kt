package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCropsFlowUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(cropsId: String): Flow<Crops> = flow {
        preferenceRepository.getGardenIdFlow().first()?.let { gardenId ->
            cropsRepository.getCropsFlow(gardenId, cropsId).collect { crops ->
                emit(crops)
            }
        }
    }
}