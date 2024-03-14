package io.tuttut.data.repository.diary

import com.google.firebase.firestore.DocumentReference
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryRepositoryIml @Inject constructor() : DiaryRepository {
    override fun getCropsDiaryList(gardenId: String, cropsId: String): Flow<Result<List<Diary>>> {
        TODO("Not yet implemented")
    }

    override fun addCropsDiary(gardenId: String, cropsId: String): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun updateCropsDiary(
        gardenId: String,
        cropsId: String,
        diaryId: String
    ): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun deleteCropsDiary(
        gardenId: String,
        cropsId: String,
        diaryId: String
    ): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun getDiaryComments(
        gardenId: String,
        cropsId: String,
        diaryId: String
    ): Flow<Result<List<Comment>>> {
        TODO("Not yet implemented")
    }

    override fun addDiaryComment(
        gardenId: String,
        cropsId: String,
        diaryId: String,
        comment: Comment
    ): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun updateDiaryComment(
        gardenId: String,
        cropsId: String,
        diaryId: String,
        comment: Comment
    ): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun deleteDiaryComment(
        gardenId: String,
        cropsId: String,
        diaryId: String,
        commentId: String
    ): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

}