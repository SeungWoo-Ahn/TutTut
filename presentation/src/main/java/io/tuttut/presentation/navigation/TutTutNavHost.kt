package io.tuttut.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.navigation.addNestedLoginGraph
import io.tuttut.presentation.ui.screen.main.navigation.addNestedMainGraph
import io.tuttut.presentation.ui.screen.main.navigation.navigateToMainGraph

@Composable
fun TutTutNavHost(
    modifier: Modifier = Modifier,
    appState: TutTutAppState,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val isAuthenticated by authViewModel.authFlow.collectAsStateWithLifecycle(initialValue = false)
    val context = LocalContext.current

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            authViewModel.login(context)
                .onSuccess {
                    appState.navController.navigateToMainGraph()
                    authViewModel.cacheBasicData()
                }
        }
    }

    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ScreenGraph.LoginGraph,
    ) {
        addNestedLoginGraph(appState)
        addNestedMainGraph(appState)
    }
}