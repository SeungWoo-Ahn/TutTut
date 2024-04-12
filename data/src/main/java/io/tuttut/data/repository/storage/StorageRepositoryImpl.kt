package io.tuttut.data.repository.storage

import android.net.Uri
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.util.deleteImage
import io.tuttut.data.util.uploadAndGetUrl
import javax.inject.Inject
import javax.inject.Named

class StorageRepositoryImpl @Inject constructor(
    @Named("diaryImageRef") val diaryImageRef: StorageReference
) : StorageRepository {

    override suspend fun uploadDiaryImage(name: String, uri: Uri): Flow<String?> = diaryImageRef.child(name).uploadAndGetUrl(uri)

    override suspend fun deleteDiaryImage(name: String): Flow<Boolean> = diaryImageRef.child(name).deleteImage()
}