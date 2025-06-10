package io.tuttut.data.util

import android.net.Uri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import io.tuttut.domain.exception.ExceptionBoundary
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.File

suspend inline fun <reified T> DocumentReference.getOneShot(): T {
    val snapshot = get().await()
    if (snapshot.exists().not()) {
        throw ExceptionBoundary.DataNotFound()
    }
    return snapshot.toObject<T>()
        ?: throw ExceptionBoundary.ConversionException()
}

suspend inline fun <reified T> Query.getOneShot(): List<T> {
    val snapShot = get().await()
    if (snapShot.isEmpty.not()) {
        throw ExceptionBoundary.DataNotFound()
    }
    return snapShot.documents.mapNotNull { it.toObject<T>()  }
}

inline fun <reified T> DocumentReference.asFlow(): Flow<T> = callbackFlow {
    val listener = addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            close(exception)
            return@addSnapshotListener
        }
        if (snapshot != null && snapshot.exists()) {
            val data = snapshot.toObject<T>()
            if (data == null) {
                close(ExceptionBoundary.ConversionException())
                return@addSnapshotListener
            }
            trySend(data)
        } else {
            close(ExceptionBoundary.DataNotFound())
            return@addSnapshotListener
        }
    }
    awaitClose { listener.remove() }
}

inline fun <reified T> Query.asFlow(): Flow<List<T>> = callbackFlow {
    val listener = addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            close(exception)
            return@addSnapshotListener
        }
        if (snapshot != null && snapshot.documents.isNotEmpty()) {
            val data = snapshot.documents.mapNotNull { it.toObject<T>() }
            trySend(data)
        } else {
            trySend(emptyList())
        }
    }
    awaitClose { listener.remove() }
}

suspend inline fun StorageReference.uploadAndGetUrl(file: File): String {
    val uri = Uri.fromFile(file)
    val snapShot = putFile(uri).await()
    val downloadUrl = snapShot.storage.downloadUrl.await()
    return downloadUrl.toString()
}