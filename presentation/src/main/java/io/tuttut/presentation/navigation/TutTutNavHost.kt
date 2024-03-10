package io.tuttut.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.addNestedLoginGraph
import io.tuttut.presentation.ui.screen.main.addNestedMainGraph

@Composable
fun TutTutNavHost(
    modifier: Modifier = Modifier,
    appState: TutTutAppState,
    startDestination: ScreenGraph
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        addNestedLoginGraph(appState)
        addNestedMainGraph(appState)
    }
}