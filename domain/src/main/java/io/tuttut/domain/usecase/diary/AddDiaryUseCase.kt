package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.AddDiaryRequest
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.image.UploadImageUseCase
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
    private val uploadImageUseCase: UploadImageUseCase,
) {
    suspend operator fun invoke(
        cropsId: String,
        content: String,
        imageList: List<ImageSource.Local>
    ): Result<String> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        val uploadedImageList = imageList.mapNotNull { image -> uploadImageUseCase(image).getOrNull() }
        val addDiaryRequest = AddDiaryRequest(
            credential = credential,
            cropsId = cropsId,
            content = content,
            imageList = uploadedImageList
        )
        diaryRepository.addDiary(addDiaryRequest)
    }
}