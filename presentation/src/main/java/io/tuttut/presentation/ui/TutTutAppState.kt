package io.tuttut.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import io.tuttut.presentation.navigation.Screen
import io.tuttut.presentation.ui.screen.login.navigateToLoginGraph
import io.tuttut.presentation.ui.screen.main.navigateToMainGraph
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
    coroutineScope: CoroutineScope
) {
    fun navigateTopLevelScreen(screen: Screen) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
        }
        when (screen) {
            Screen.Login -> navController.navigateToLoginGraph(topLevelNavOptions)
            Screen.Main -> navController.navigateToMainGraph(topLevelNavOptions)
            else -> Unit
        }
    }
}