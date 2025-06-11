package io.tuttut.domain.usecase.comment

import io.tuttut.domain.model.comment.Comment
import io.tuttut.domain.repository.CommentRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCommentListFlowUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(diaryId: String): Flow<List<Comment>> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                commentRepository.getCommentListFlow(credential.gardenId, diaryId)
            }
            .catch {
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
}