package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.UpdateDiaryRequest
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.image.UploadImageUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
    private val uploadImageUseCase: UploadImageUseCase,
) {
    suspend operator fun invoke(
        id: String,
        content: String,
        imageList: List<ImageSource>
    ): Result<Unit> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        val uploadedImageList = imageList.mapNotNull { image -> uploadImageUseCase(image).getOrNull() }
        val updateDiaryRequest = UpdateDiaryRequest(
            id = id,
            gardenId = credential.gardenId,
            content = content,
            imageList = uploadedImageList
        )
        diaryRepository.updateDiary(updateDiaryRequest)
    }
}