package io.tuttut.domain.util

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> runCatchingExceptCancel(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}