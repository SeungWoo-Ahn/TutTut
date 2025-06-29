package io.tuttut.domain.usecase.comment

import io.tuttut.domain.model.comment.CommentWithAuthor
import io.tuttut.domain.model.comment.withAuthor
import io.tuttut.domain.repository.CommentRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.usecase.user.GetGardenUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCommentListFlowUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val preferenceRepository: PreferenceRepository,
    private val getGardenUserUseCase: GetGardenUserUseCase,
) {
    operator fun invoke(diaryId: String): Flow<List<CommentWithAuthor>> =
        preferenceRepository
            .getCredentialFlow()
            .flatMapLatest { credential ->
                commentRepository
                    .getCommentListFlow(credential.gardenId, diaryId)
                    .map { commentList ->
                        commentList.map { comment ->
                            comment.withAuthor(
                                author = getGardenUserUseCase(comment.authorId, credential.gardenId),
                                isMine = comment.authorId == credential.userId,
                            )
                        }
                    }
            }
            .catch {
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
}