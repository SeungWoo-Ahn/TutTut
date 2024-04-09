package io.tuttut.data.repository.storage

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import io.tuttut.data.model.response.Result

interface StorageRepository {

    suspend fun uploadDiaryImage(name: String, uri: Uri): Flow<String?>

    fun deleteDiaryImage(name: String): Flow<Result<Void>>
}