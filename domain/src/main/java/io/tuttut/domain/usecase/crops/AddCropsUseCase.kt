package io.tuttut.domain.usecase.crops

import io.tuttut.domain.model.crops.AddCropsRequest
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddCropsUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(addCropsRequest: AddCropsRequest): Result<String> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        cropsRepository.addCrops(credential.gardenId, addCropsRequest)
    }
}
