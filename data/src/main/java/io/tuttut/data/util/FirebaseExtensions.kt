package io.tuttut.data.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.toObject
import io.tuttut.domain.exception.ExceptionBoundary
import kotlinx.coroutines.tasks.await

suspend inline fun <reified T> DocumentReference.getOneShot(): T {
    val snapshot = get().await()
    if (snapshot.exists().not()) {
        throw ExceptionBoundary.DataNotFound()
    }
    return snapshot.toObject<T>()
        ?: throw ExceptionBoundary.ConversionException()
}