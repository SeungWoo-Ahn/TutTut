package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.Diary
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDiaryFlowUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(diaryId: String): Flow<Diary> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                diaryRepository.getDiaryFlow(credential.gardenId, diaryId)
            }
            .flowOn(Dispatchers.IO)
}