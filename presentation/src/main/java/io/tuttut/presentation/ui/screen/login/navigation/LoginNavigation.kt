package io.tuttut.presentation.ui.screen.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.presentation.navigation.Screen
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.navigation.enterAnimation
import io.tuttut.presentation.navigation.popEnterAnimation
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.LoginRoute
import io.tuttut.presentation.ui.screen.login.participate.ParticipateRoute
import io.tuttut.presentation.ui.screen.login.welcome.WelcomeRoute

fun NavController.navigateToLoginGraph() = navigate(Screen.Login.route) {
    popUpTo(ScreenGraph.MainGraph.route) { inclusive = true }
}

fun NavGraphBuilder.addNestedLoginGraph(appState: TutTutAppState, onShowSnackBar: suspend (String, String?) -> Boolean) {
    navigation(startDestination = Screen.Login.route, route = ScreenGraph.LoginGraph.route) {
        composable(
            route = Screen.Login.route,
            popEnterTransition = popEnterAnimation()
        ) {
            LoginRoute(
                onNext = { appState.navigate(Screen.Participate) },
                moveMain = { appState.navigateTopLevelScreen(ScreenGraph.MainGraph) },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.Participate.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            ParticipateRoute(
                onNext = { appState.navigate(Screen.Welcome) },
                onBack = { appState.popBackStack() },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.Welcome.route,
            enterTransition = enterAnimation(),
        ) {
            WelcomeRoute { appState.navigateTopLevelScreen(ScreenGraph.MainGraph) }
        }
    }
}