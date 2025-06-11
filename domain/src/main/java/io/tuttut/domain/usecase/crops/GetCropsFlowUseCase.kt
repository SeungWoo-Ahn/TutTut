package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCropsFlowUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(cropsId: String): Flow<Crops> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                cropsRepository.getCropsFlow(credential.gardenId, cropsId)
            }
            .flowOn(Dispatchers.IO)
}