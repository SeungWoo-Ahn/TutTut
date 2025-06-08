package io.tuttut.domain.usecase.comment

import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.comment.AddCommentRequest
import io.tuttut.domain.repository.CommentRepository
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val preferenceRepository: PreferenceRepository,
)  {
    suspend operator fun invoke(diaryId: String, content: String): Result<Unit> = runCatching {
        val authorId = preferenceRepository.getUserIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val gardenId = preferenceRepository.getGardenIdFlow().first()
            ?: throw ExceptionBoundary.UnAuthenticated()
        val addCommentRequest = AddCommentRequest(
            gardenId = gardenId,
            diaryId = diaryId,
            authorId = authorId,
            content = content,
        )
        commentRepository.addComment(addCommentRequest)
    }
}