package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.DiaryWithAuthor
import io.tuttut.domain.model.diary.UpdateDiaryRequest
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.image.SaveLocation
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.image.DeleteImageUseCase
import io.tuttut.domain.usecase.image.UploadImageUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
    private val getDiaryFlowUseCase: GetDiaryFlowUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
) {
    suspend operator fun invoke(
        id: String,
        content: String,
        imageList: List<ImageSource>,
    ): Result<Unit> = runCatchingExceptCancel {
        val diary = getDiaryFlowUseCase(id).first()
        if (isDiaryDataChanged(diary, content, imageList)) {
            val credential = preferenceRepository.getCredentialFlow().first()
            val uploadedImageList = imageList
                .mapNotNull { image ->
                    uploadImageUseCase(image, SaveLocation.DIARY).getOrNull()
                }
            val updateDiaryRequest = UpdateDiaryRequest(
                id = id,
                gardenId = credential.gardenId,
                content = content,
                imageList = uploadedImageList
            )
            diaryRepository.updateDiary(updateDiaryRequest)
                .also {
                    diary.imageList
                        .filter { it !in imageList }
                        .forEach { image ->
                            deleteImageUseCase(image, SaveLocation.DIARY)
                        }
                }
        }
    }

    private fun isDiaryDataChanged(
        diary: DiaryWithAuthor,
        content: String,
        imageList: List<ImageSource>
    ): Boolean =
        diary.content != content || diary.imageList != imageList
}