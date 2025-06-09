package io.tuttut.data.repository.storage

import android.net.Uri
import com.google.firebase.storage.StorageReference
import io.tuttut.data.network.model.StorageImage
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.util.deleteImage
import io.tuttut.data.util.uploadAndGetUrl
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class StorageRepositoryImpl @Inject constructor(
    @Named("diaryImageRef") val diaryImageRef: StorageReference,
    @Named("profileImageRef") val profileImageRef: StorageReference
) : StorageRepository {
    override suspend fun uploadDiaryImage(name: String, uri: Uri): Flow<String?> = diaryImageRef.child(name).uploadAndGetUrl(uri)

    override suspend fun uploadProfileImage(name: String, uri: Uri): Flow<String?> = profileImageRef.child(name).uploadAndGetUrl(uri)

    override suspend fun deleteDiaryImage(name: String): Flow<Boolean> = diaryImageRef.child(name).deleteImage()

    override suspend fun deleteProfileImage(name: String): Flow<Boolean> = profileImageRef.child(name).deleteImage()

    override suspend fun deleteAllImages(imageList: List<StorageImage>) {
        imageList.forEach { image ->
            diaryImageRef.child(image.name).delete().await()
        }
    }
}