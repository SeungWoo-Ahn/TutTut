package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.PutCropsRequest
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PutCropsUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(putCropsRequest: PutCropsRequest): Result<String> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        cropsRepository.putCrops(credential.gardenId, putCropsRequest)
    }
}