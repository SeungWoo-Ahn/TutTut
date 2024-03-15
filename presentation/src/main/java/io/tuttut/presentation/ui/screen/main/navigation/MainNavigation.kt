package io.tuttut.presentation.ui.screen.main.navigation

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
import io.tuttut.presentation.ui.screen.main.MainRoute
import io.tuttut.presentation.ui.screen.main.addCrops.AddCropsRoute
import io.tuttut.presentation.ui.screen.main.cropsInfoDetail.CropsInfoDetailRoute
import io.tuttut.presentation.ui.screen.main.selectCrops.SelectCropsRoute

fun NavController.navigateToMainGraph() = navigate(Screen.Main.route) {
    popUpTo(ScreenGraph.LoginGraph.route) { inclusive = true }
}

fun NavGraphBuilder.addNestedMainGraph(appState: TutTutAppState) {
    navigation(startDestination = Screen.Main.route, route = ScreenGraph.MainGraph.route) {
        composable(
            route = Screen.Main.route,
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            MainRoute(
                moveRecommend = { appState.navController.navigate(Screen.SelectCrops.route) },
                moveMy = {  }
            )
        }
        composable(
            route = Screen.SelectCrops.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            SelectCropsRoute(
                onBack = { appState.navController.popBackStack() },
                moveDetail = { appState.navController.navigate(Screen.CropsInfoDetail.route) },
                moveAdd = { appState.navController.navigate(Screen.AddCrops.route) }
            )
        }
        composable(
            route = Screen.CropsInfoDetail.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            CropsInfoDetailRoute(
                onBack = { appState.navController.popBackStack() },
                onItemClick = {  },
                moveAdd = { appState.navController.navigate(Screen.AddCrops.route) }
            )
        }
        composable(
            route = Screen.AddCrops.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
        ) {
            AddCropsRoute(
                onBack = { appState.navController.popBackStack() },
                onButton = {  }
            )
        }
    }
}