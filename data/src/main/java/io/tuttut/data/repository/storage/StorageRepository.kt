package io.tuttut.data.repository.storage

import android.net.Uri
import io.tuttut.data.model.dto.StorageImage
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    suspend fun uploadDiaryImage(name: String, uri: Uri): Flow<String?>

    suspend fun deleteDiaryImage(name: String): Flow<Boolean>

    suspend fun deleteAllImages(imageList: List<StorageImage>)
}