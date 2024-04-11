package io.tuttut.presentation.ui.screen.main.navigation

import android.os.Build
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
import io.tuttut.presentation.ui.screen.main.addDiary.AddDiaryRoute
import io.tuttut.presentation.ui.screen.main.cropsDetail.CropsDetailRoute
import io.tuttut.presentation.ui.screen.main.cropsInfoDetail.CropsInfoDetailRoute
import io.tuttut.presentation.ui.screen.main.diaryDetail.DiaryDetailRoute
import io.tuttut.presentation.ui.screen.main.diaryList.DiaryListRoute
import io.tuttut.presentation.ui.screen.main.recipeWebView.RecipeWebRoute
import io.tuttut.presentation.ui.screen.main.selectCrops.SelectCropsRoute

fun NavController.navigateToMainGraph() = navigate(Screen.Main.route) {
    popUpTo(ScreenGraph.LoginGraph.route) { inclusive = true }
}

fun NavGraphBuilder.addNestedMainGraph(appState: TutTutAppState, onShowSnackBar: suspend (String, String?) -> Boolean) {
    navigation(startDestination = Screen.Main.route, route = ScreenGraph.MainGraph.route) {
        composable(
            route = Screen.Main.route,
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            MainRoute(
                moveRecommend = { appState.navController.navigate(Screen.SelectCrops.route) },
                moveMy = {  },
                moveDetail = { appState.navController.navigate(Screen.CropsDetail.route) }
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
                moveAdd = { appState.navController.navigate(Screen.AddCrops.route) },
                moveRecipeWeb = { appState.navController.navigate(Screen.RecipeWeb.route) }
            )
        }
        composable(
            route = Screen.AddCrops.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
        ) {
            AddCropsRoute(
                scope = appState.coroutineScope,
                onBack = { appState.navController.popBackStack() },
                onButton = {
                    appState.navController.navigate(Screen.CropsDetail.route) {
                        launchSingleTop = true
                        popUpTo(Screen.Main.route)
                    }
                },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.CropsDetail.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            CropsDetailRoute(
                scope = appState.coroutineScope,
                onBack = { appState.navController.popBackStack() },
                moveCropsInfo = { appState.navController.navigate(Screen.CropsInfoDetail.route) },
                moveEditCrops = { appState.navController.navigate(Screen.AddCrops.route) },
                moveDiaryList = { appState.navController.navigate(Screen.DiaryList.route) },
                moveDiaryDetail = { appState.navController.navigate(Screen.DiaryDetail.route) },
                moveAddDiary = { appState.navController.navigate(Screen.AddDiary.route) },
                moveMain = {
                    appState.navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                moveRecipeWeb = { appState.navController.navigate(Screen.RecipeWeb.route) },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.RecipeWeb.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            RecipeWebRoute(
                onBack = { appState.navController.popBackStack() }
            )
        }
        composable(
            route = Screen.DiaryList.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            DiaryListRoute(
                scope = appState.coroutineScope,
                onBack = { appState.navController.popBackStack() },
                moveDiary = { appState.navController.navigate(Screen.DiaryDetail.route) }
            )
        }
        composable(
            route = Screen.AddDiary.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AddDiaryRoute(
                    onBack = { appState.navController.popBackStack() },
                    onShowSnackBar = onShowSnackBar
                )
            }
        }
        composable(
            route = Screen.DiaryDetail.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(easing = LinearEasing)) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(easing = LinearEasing)) }
        ) {
            DiaryDetailRoute(
                onBack = { appState.navController.popBackStack() },
                onShowSnackBar = onShowSnackBar
            )
        }
    }
}