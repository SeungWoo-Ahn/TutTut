package io.tuttut.presentation.ui.screen.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.presentation.navigation.Screen
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.participate.ParticipateRoute
import io.tuttut.presentation.ui.screen.login.welcome.WelcomeRoute

fun NavController.navigateToLoginGraph(navOptions: NavOptions) = navigate(ScreenGraph.LoginGraph.route, navOptions)

fun NavGraphBuilder.addNestedLoginGraph(appState: TutTutAppState) {
    navigation(startDestination = Screen.Login.route, route = ScreenGraph.LoginGraph.route) {
        composable(Screen.Login.route) {
            LoginRoute(onNext = { appState.navController.navigate(Screen.Participate.route) })
        }
        composable(Screen.Participate.route) {
            ParticipateRoute(
                onNext = { appState.navController.navigate(Screen.Welcome.route) },
                onBack = { appState.navController.popBackStack() }
            )
        }
        composable(Screen.Welcome.route) {
            WelcomeRoute { appState.navigateTopLevelScreen(ScreenGraph.MainGraph) }
        }
    }
}