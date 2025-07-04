package io.tuttut.presentation.ui.screen.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.presentation.navigation.LoginScreen
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.LoginRoute
import io.tuttut.presentation.ui.screen.login.participate.ParticipateRoute
import io.tuttut.presentation.ui.screen.login.welcome.WelcomeRoute
import io.tuttut.presentation.ui.screen.main.navigation.navigateToMainGraph

fun NavGraphBuilder.addNestedLoginGraph(appState: TutTutAppState) {
    val navController = appState.navController

    navigation<ScreenGraph.LoginGraph>(
        startDestination = LoginScreen.Login
    ) {
        composable<LoginScreen.Login> {
            LoginRoute(
                moveParticipate = navController::navigateToParticipateScreen,
            )
        }
        composable<LoginScreen.Participate> {
            ParticipateRoute(
                moveWelcome = navController::navigateToWelcomeScreen,
                onBack = navController::popBackStack,
            )
        }
        composable<LoginScreen.Welcome> {
            WelcomeRoute(
                moveMain = navController::navigateToMainGraph
            )
        }
    }
}

fun NavController.navigateToLoginGraph() = navigate(ScreenGraph.LoginGraph) {
    popUpTo(graph.id) { inclusive = true }
}

private fun NavController.navigateToParticipateScreen(userId: String) =
    navigate(LoginScreen.Participate(userId))

private fun NavController.navigateToWelcomeScreen() = navigate(LoginScreen.Welcome)
