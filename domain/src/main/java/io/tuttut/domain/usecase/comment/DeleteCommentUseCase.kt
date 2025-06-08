package io.tuttut.domain.usecase.comment

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.comment.DeleteCommentRequest
import io.tuttut.domain.repository.CommentRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    suspend operator fun invoke(diaryId: String, commentId: String): Result<Unit> = runCatching {
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val deleteCommentRequest = DeleteCommentRequest(
            gardenId = gardenId,
            diaryId = diaryId,
            commentId = commentId,
        )
        commentRepository.deleteComment(deleteCommentRequest)
    }
}