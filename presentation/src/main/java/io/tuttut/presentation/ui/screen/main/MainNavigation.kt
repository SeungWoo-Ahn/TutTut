package io.tuttut.presentation.ui.screen.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.presentation.navigation.Screen
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.main.addCrops.AddCropsRoute
import io.tuttut.presentation.ui.screen.main.cropsInfoDetail.CropsInfoDetailRoute
import io.tuttut.presentation.ui.screen.main.selectCrops.SelectCropsRoute

fun NavController.navigateToMainGraph() = navigate(Screen.Main.route) {
    popUpTo(ScreenGraph.LoginGraph.route) { inclusive = true }
}

fun NavGraphBuilder.addNestedMainGraph(appState: TutTutAppState) {
    navigation(startDestination = Screen.Main.route, route = ScreenGraph.MainGraph.route) {
        composable(Screen.Main.route) {
            MainRoute(
                moveRecommend = { appState.navController.navigate(Screen.SelectCrops.route) },
                moveMy = {  }
            )
        }
        composable(Screen.SelectCrops.route) {
            SelectCropsRoute(
                onBack = { appState.navController.popBackStack() },
                moveDetail = { appState.navController.navigate(Screen.CropsInfoDetail.route) },
                moveAdd = { appState.navController.navigate(Screen.AddCrops.route) }
            )
        }
        composable(Screen.CropsInfoDetail.route) {
            CropsInfoDetailRoute(
                onBack = { appState.navController.popBackStack() },
                onItemClick = {  },
                onButton = { appState.navController.navigate(Screen.AddCrops.route) }
            )
        }
        composable(Screen.AddCrops.route) {
            AddCropsRoute(
                onBack = { appState.navController.popBackStack() },
                onButton = {  }
            )
        }
    }
}