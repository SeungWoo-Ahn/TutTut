package io.tuttut.domain.usecase.crops

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.repository.CropsRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteCropsUseCase @Inject constructor(
    private val cropsRepository: CropsRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(cropsId: String): Result<Unit> = runCatching {
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        cropsRepository.deleteCrops(gardenId, cropsId)
    }
}