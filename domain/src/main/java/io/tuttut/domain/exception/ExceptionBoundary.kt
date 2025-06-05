package io.tuttut.domain.exception

sealed class ExceptionBoundary : Exception() {
    class UnAuthenticated : ExceptionBoundary()
    class UserNotFound : ExceptionBoundary()
    class GardenNotFound : ExceptionBoundary()
}