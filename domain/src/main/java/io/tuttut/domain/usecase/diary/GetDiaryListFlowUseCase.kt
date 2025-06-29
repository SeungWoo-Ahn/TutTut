package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.DiaryWithAuthor
import io.tuttut.domain.model.diary.toDiaryWithAuthor
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.user.GetGardenUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiaryListFlowUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
    private val getGardenUserUseCase: GetGardenUserUseCase,
) {
    operator fun invoke(cropsId: String): Flow<List<DiaryWithAuthor>> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                diaryRepository
                    .getDiaryListFlow(credential.gardenId, cropsId)
                    .map { diaryList ->
                        diaryList.map { diary ->
                            diary.toDiaryWithAuthor(
                                author = getGardenUserUseCase(diary.authorId, credential.gardenId),
                                isMine = diary.authorId == credential.userId,
                            )
                        }
                    }
            }
            .catch {
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
}