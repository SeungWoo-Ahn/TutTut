package io.tuttut.presentation.ui.screen.login.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.presentation.navigation.Screen
import io.tuttut.presentation.navigation.ScreenGraph
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
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            LoginRoute(
                onNext = { appState.navController.navigate(Screen.Participate.route) },
                moveMain = { appState.navigateTopLevelScreen(ScreenGraph.MainGraph) },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.Participate.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            ParticipateRoute(
                onNext = { appState.navController.navigate(Screen.Welcome.route) },
                onBack = { appState.navController.popBackStack() },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.Welcome.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
        ) {
            WelcomeRoute { appState.navigateTopLevelScreen(ScreenGraph.MainGraph) }
        }
    }
}