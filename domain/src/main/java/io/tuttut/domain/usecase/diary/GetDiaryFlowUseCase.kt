package io.tuttut.domain.usecase.diary

import io.tuttut.domain.model.diary.DiaryWithAuthor
import io.tuttut.domain.model.diary.withAuthor
import io.tuttut.domain.repository.DiaryRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.user.GetGardenUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiaryFlowUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val preferenceRepository: PreferenceRepository,
    private val getGardenUserUseCase: GetGardenUserUseCase,
) {
    operator fun invoke(diaryId: String): Flow<DiaryWithAuthor> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                diaryRepository
                    .getDiaryFlow(credential.gardenId, diaryId)
                    .map { diary ->
                        diary.withAuthor(
                            author = getGardenUserUseCase(diary.authorId, credential.gardenId),
                            isMine = diary.authorId == credential.userId,
                        )
                    }
            }
            .flowOn(Dispatchers.IO)
}