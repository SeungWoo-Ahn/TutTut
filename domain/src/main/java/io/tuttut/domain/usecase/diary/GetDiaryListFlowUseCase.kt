package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.Diary
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDiaryListFlowUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(cropsId: String): Flow<List<Diary>> =
        preferenceRepository
            .getGardenIdFlow()
            .filterNotNull()
            .flatMapLatest { gardenId ->
                diaryRepository.getDiaryListFlow(gardenId, cropsId)
            }
            .flowOn(Dispatchers.IO)
}