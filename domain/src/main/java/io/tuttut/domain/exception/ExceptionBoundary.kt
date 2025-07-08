package io.tuttut.domain.exception

sealed class ExceptionBoundary : Exception() {
    class UnAuthenticated : ExceptionBoundary()
    class DataNotFound : ExceptionBoundary()
    class GardenNotFound : ExceptionBoundary()
    class ConversionException : ExceptionBoundary()
}