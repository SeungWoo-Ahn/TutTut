package io.tuttut.domain.usecase.diary

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.diary.DeleteDiaryRequest
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(id: String, cropsId: String): Result<Unit> = runCatching {
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val deleteDiaryRequest = DeleteDiaryRequest(
            id = id,
            gardenId = gardenId,
            cropsId = cropsId
        )
        diaryRepository.deleteDiary(deleteDiaryRequest)
    }
}