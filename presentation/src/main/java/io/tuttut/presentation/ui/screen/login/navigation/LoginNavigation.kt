package io.tuttut.presentation.ui.screen.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.presentation.navigation.LoginScreen
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.LoginRoute
import io.tuttut.presentation.ui.screen.login.participate.ParticipateRoute
import io.tuttut.presentation.ui.screen.login.welcome.WelcomeRoute
import io.tuttut.presentation.ui.screen.main.navigation.navigateToMainGraph

fun NavGraphBuilder.addNestedLoginGraph(
    appState: TutTutAppState,
    onShowSnackBar: suspend (String, String?) -> Boolean
) {
    val navController = appState.navController

    navigation<ScreenGraph.LoginGraph>(
        startDestination = LoginScreen.Login
    ) {
        composable<LoginScreen.Login> {
            LoginRoute(
                moveParticipate = navController::navigateToParticipateScreen,
                moveMain = navController::navigateToMainGraph,
            )
        }
        composable<LoginScreen.Participate> {
            ParticipateRoute(
                moveWelcome = navController::navigateToWelcomeScreen,
                onBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar
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

private fun NavController.navigateToParticipateScreen(joinRequest: JoinRequest) =
    navigate(LoginScreen.Participate(joinRequest))

private fun NavController.navigateToWelcomeScreen() = navigate(LoginScreen.Welcome)
