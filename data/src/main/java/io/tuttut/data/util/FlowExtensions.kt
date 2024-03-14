package io.tuttut.data.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import io.tuttut.data.model.response.Result
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun <T> DocumentReference.asSnapShotFlow(
    dataType: Class<T>,
    additionalWork: ((T) -> Unit)? = null
): Flow<Result<T>> = callbackFlow {
    trySend(Result.Loading)
    val registration = addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            trySend(Result.Error(exception))
            cancel()
            return@addSnapshotListener
        }
        if (snapshot != null && snapshot.exists()) {
            val data = snapshot.toObject(dataType) as T
            additionalWork?.invoke(data)
            trySend(Result.Success(data))
        } else {
            trySend(Result.NotFound)
        }
    }
    awaitClose { registration.remove() }
}


fun <T> Query.asFlow(
    dataType: Class<T>,
    additionalWork: ((List<T>) -> Unit)? = null
): Flow<Result<List<T>>> = callbackFlow {
    trySend(Result.Loading)
    val callback = addSnapshotListener { snapshots, exception ->
        if (exception != null) {
            trySend(Result.Error(exception))
            close(exception)
        }
        if (snapshots != null) {
            if (snapshots.documents.isNotEmpty()) {
                val data = snapshots.map { it.toObject(dataType) }
                additionalWork?.invoke(data)
                trySend(Result.Success(data))
            } else {
                trySend(Result.Success(emptyList()))
            }
        } else {
            trySend(Result.NotFound)
        }
    }
    awaitClose { callback.remove() }
}

fun <T> Query.paginate(dataType: Class<T>, limit: Long, lastVisibleItem: Flow<Int>): Flow<Result<List<T>>> = flow {
    val documents = mutableListOf<T>()
    documents.addAll(
        suspendCoroutine { c ->
            this@paginate.limit(limit).get().addOnSuccessListener { snapshots ->
                val data = snapshots.map { it.toObject(dataType) }
                c.resume(data)
            }
        }
    )
    emit(Result.Success(documents))
    lastVisibleItem.transform { lastVisible ->
        if (lastVisible == documents.size && documents.isNotEmpty()) {
            documents.addAll(
                suspendCoroutine { c ->
                    this@paginate.startAfter(documents.last())
                        .limit(limit)
                        .get()
                        .addOnSuccessListener { snapshots ->
                            val data = snapshots.map { it.toObject(dataType) }
                            c.resume(data)
                        }
                }
            )
            emit(Result.Success(documents))
        }
    }.collect { docs ->
        emit(docs)
    }
}