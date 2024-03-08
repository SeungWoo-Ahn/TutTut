package io.tuttut.presentation.navigation

sealed class ScreenGraph(val route: String) {
    data object LoginGraph : ScreenGraph("loginGraph")
    data object MainGraph : ScreenGraph("mainGraph")
}