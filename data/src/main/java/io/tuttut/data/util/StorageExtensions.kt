package io.tuttut.data.util

import android.net.Uri
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

suspend fun StorageReference.uploadAndGetUrl(uri: Uri): Flow<String?> = callbackFlow {
    val uploadTask = putFile(uri)
    val listener = uploadTask
        .addOnFailureListener { trySend(null) }
        .addOnSuccessListener {
            uploadTask.continueWithTask {
                downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) trySend(task.result.toString())
                else trySend(null)
            }
        }
    awaitClose { listener.cancel() }
}