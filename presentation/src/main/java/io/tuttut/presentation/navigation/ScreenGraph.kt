package io.tuttut.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ScreenGraph {
    @Serializable
    data object LoginGraph : ScreenGraph

    @Serializable
    data object MainGraph : ScreenGraph
}