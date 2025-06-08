package io.tuttut.domain.usecase.comment

import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteAllCommentUseCase @Inject constructor(
    private val getCommentListFlowUseCase: GetCommentListFlowUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
) {
    suspend operator fun invoke(diaryId: String): Result<Unit> = runCatching {
        getCommentListFlowUseCase(diaryId)
            .first()
            .forEach { comment ->
                deleteCommentUseCase(diaryId, comment.id)
            }
    }
}