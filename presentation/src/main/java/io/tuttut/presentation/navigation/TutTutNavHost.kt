package io.tuttut.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.navigation.addNestedLoginGraph
import io.tuttut.presentation.ui.screen.main.navigation.addNestedMainGraph

@Composable
fun TutTutNavHost(
    modifier: Modifier = Modifier,
    appState: TutTutAppState,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ScreenGraph.LoginGraph,
    ) {
        addNestedLoginGraph(appState)
        addNestedMainGraph(appState, onShowSnackBar)
    }
}