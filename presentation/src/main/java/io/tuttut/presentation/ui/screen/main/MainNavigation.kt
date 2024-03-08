package io.tuttut.presentation.ui.screen.main

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import io.tuttut.presentation.navigation.ScreenGraph

fun NavController.navigateToMainGraph(navOptions: NavOptions) = navigate(ScreenGraph.MainGraph.route, navOptions)