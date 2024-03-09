package io.tuttut.presentation.ui.screen.login

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import io.tuttut.presentation.util.GoogleAuthClient

fun NavController.navigateToLoginGraph(navOptions: NavOptions) = navigate(ScreenGraph.LoginGraph.route, navOptions)

fun NavGraphBuilder.addNestedLoginGraph(appState: TutTutAppState, googleAuthClient: GoogleAuthClient) {
    navigation(startDestination = Screen.Login.route, route = ScreenGraph.LoginGraph.route) {
        composable(
            route = Screen.Login.route,
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            LoginRoute(
                coroutineScope = appState.coroutineScope,
                googleAuthClient = googleAuthClient,
                onNext = { appState.navController.navigate(Screen.Participate.route) },
                moveMain = { appState.navigateTopLevelScreen(ScreenGraph.MainGraph) }
            )
        }
        composable(
            route = Screen.Participate.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            ParticipateRoute(
                onNext = { appState.navController.navigate(Screen.Welcome.route) },
                onBack = { appState.navController.popBackStack() }
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