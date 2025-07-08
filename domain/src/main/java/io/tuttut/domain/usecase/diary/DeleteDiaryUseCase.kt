package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.DeleteDiaryRequest
import io.tuttut.domain.model.image.SaveLocation
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.image.DeleteImageUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
    private val getDiaryFlowUseCase: GetDiaryFlowUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
) {
    suspend operator fun invoke(id: String): Result<Unit> = runCatchingExceptCancel {
        getDiaryFlowUseCase(id)
            .first()
            .let { diary ->
                val credential = preferenceRepository.getCredentialFlow().first()
                val deleteDiaryRequest = DeleteDiaryRequest(
                    id = id,
                    gardenId = credential.gardenId,
                    cropsId = diary.cropsId,
                )
                diaryRepository.deleteDiary(deleteDiaryRequest)
                diary.imageList.forEach { image ->
                    deleteImageUseCase(image, SaveLocation.DIARY)
                }
            }
    }
}