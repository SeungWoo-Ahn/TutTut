package io.tuttut.data.repository

import com.google.firebase.storage.StorageReference
import io.tuttut.data.network.di.DiaryImageReference
import io.tuttut.data.network.di.ProfileImageReference
import io.tuttut.data.util.uploadAndGetUrl
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.model.image.SaveLocation
import io.tuttut.domain.repository.ImageRepository
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    @DiaryImageReference val diaryImageRef: StorageReference,
    @ProfileImageReference val profileImageRef: StorageReference,
) : ImageRepository {
    private fun getImageRef(location: SaveLocation): StorageReference =
        when (location) {
            SaveLocation.DIARY -> diaryImageRef
            SaveLocation.USER -> profileImageRef
        }

    override suspend fun uploadImage(file: File, location: SaveLocation): String {
        return getImageRef(location)
            .child(file.name)
            .uploadAndGetUrl(file)
    }

    override suspend fun deleteImage(image: ImageSource.Remote, location: SaveLocation) {
        getImageRef(location)
            .child(image.name)
            .delete()
            .await()
    }
}