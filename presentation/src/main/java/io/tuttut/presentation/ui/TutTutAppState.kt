package io.tuttut.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.ui.screen.login.navigation.navigateToLoginGraph
import io.tuttut.presentation.ui.screen.main.navigation.navigateToMainGraph
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberTutTutAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) : TutTutAppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        TutTutAppState(
            navController = navController,
            coroutineScope = coroutineScope
        )
    }
}

@Stable
class TutTutAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {
    fun navigateTopLevelScreen(destination: ScreenGraph) {
        when (destination) {
            ScreenGraph.LoginGraph -> navController.navigateToLoginGraph()
            ScreenGraph.MainGraph -> navController.navigateToMainGraph()
        }
    }
}