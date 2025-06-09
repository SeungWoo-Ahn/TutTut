package io.tuttut.domain.usecase.comment

import io.tuttut.domain.model.comment.AddCommentRequest
import io.tuttut.domain.repository.CommentRepository
import io.tuttut.domain.repository.PreferenceRepository
import io.tuttut.domain.util.runCatchingExceptCancel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val preferenceRepository: PreferenceRepository,
)  {
    suspend operator fun invoke(diaryId: String, content: String): Result<Unit> = runCatchingExceptCancel {
        val credential = preferenceRepository.getCredentialFlow().first()
        val addCommentRequest = AddCommentRequest(
            credential = credential,
            diaryId = diaryId,
            content = content,
        )
        commentRepository.addComment(addCommentRequest)
    }
}