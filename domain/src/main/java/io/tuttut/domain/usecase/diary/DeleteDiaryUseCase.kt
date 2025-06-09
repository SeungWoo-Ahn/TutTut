package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.DeleteDiaryRequest
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(id: String, cropsId: String): Result<Unit> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        val deleteDiaryRequest = DeleteDiaryRequest(
            id = id,
            gardenId = credential.gardenId,
            cropsId = cropsId
        )
        diaryRepository.deleteDiary(deleteDiaryRequest)
    }
}