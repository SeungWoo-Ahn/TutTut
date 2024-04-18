package io.tuttut.data.model.response

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object NotFound : Result<Nothing>
    data object Loading : Result<Nothing>
}
