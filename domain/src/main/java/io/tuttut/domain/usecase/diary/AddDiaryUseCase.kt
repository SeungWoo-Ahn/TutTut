package io.tuttut.domain.usecase.diary

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.diary.AddDiaryRequest
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.image.UploadImageUseCase
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
    ): Result<Unit> = runCatching {
        val authorId = preferenceRepository.getUserIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val addDiaryRequest = AddDiaryRequest(
            authorId = authorId,
            gardenId = gardenId,
            cropsId = cropsId,
            content = content,
            imageList = imageList.mapNotNull { image -> uploadImageUseCase(image).getOrNull() }
        )
        diaryRepository.addDiary(addDiaryRequest)
    }
}