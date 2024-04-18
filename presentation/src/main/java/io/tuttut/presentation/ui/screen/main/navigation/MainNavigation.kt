package io.tuttut.presentation.ui.screen.main.navigation

import android.os.Build
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.presentation.navigation.Screen
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.navigation.enterAnimation
import io.tuttut.presentation.navigation.popEnterAnimation
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.main.MainRoute
import io.tuttut.presentation.ui.screen.main.addCrops.AddCropsRoute
import io.tuttut.presentation.ui.screen.main.addDiary.AddDiaryRoute
import io.tuttut.presentation.ui.screen.main.changeGarden.ChangeGardenRoute
import io.tuttut.presentation.ui.screen.main.changeProfile.ChangeProfileRoute
import io.tuttut.presentation.ui.screen.main.cropsDetail.CropsDetailRoute
import io.tuttut.presentation.ui.screen.main.cropsInfoDetail.CropsInfoDetailRoute
import io.tuttut.presentation.ui.screen.main.diaryDetail.DiaryDetailRoute
import io.tuttut.presentation.ui.screen.main.diaryList.DiaryListRoute
import io.tuttut.presentation.ui.screen.main.my.MyRoute
import io.tuttut.presentation.ui.screen.main.recipeWebView.RecipeWebRoute
import io.tuttut.presentation.ui.screen.main.selectCrops.SelectCropsRoute
import io.tuttut.presentation.ui.screen.main.setting.SettingRoute

fun NavController.navigateToMainGraph() = navigate(Screen.Main.route) {
    popUpTo(ScreenGraph.LoginGraph.route) { inclusive = true }
}

fun NavGraphBuilder.addNestedMainGraph(appState: TutTutAppState, onShowSnackBar: suspend (String, String?) -> Boolean) {
    navigation(startDestination = Screen.Main.route, route = ScreenGraph.MainGraph.route) {
        composable(
            route = Screen.Main.route,
            popEnterTransition = popEnterAnimation()
        ) {
            MainRoute(
                moveRecommend = { appState.navigate(Screen.SelectCrops) },
                moveMy = { appState.navigate(Screen.My) },
                moveDetail = { appState.navigate(Screen.CropsDetail) }
            )
        }
        composable(
            route = Screen.SelectCrops.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            SelectCropsRoute(
                onBack = { appState.popBackStack() },
                moveDetail = { appState.navigate(Screen.CropsInfoDetail) },
                moveAdd = { appState.navigate(Screen.AddCrops) }
            )
        }
        composable(
            route = Screen.CropsInfoDetail.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            CropsInfoDetailRoute(
                onBack = { appState.popBackStack() },
                moveAdd = { appState.navigate(Screen.AddCrops) },
                moveRecipeWeb = { appState.navigate(Screen.RecipeWeb) }
            )
        }
        composable(
            route = Screen.AddCrops.route,
            enterTransition = enterAnimation(),
        ) {
            AddCropsRoute(
                scope = appState.coroutineScope,
                onBack = { appState.popBackStack() },
                onButton = {
                    appState.navigateWithOptions(Screen.CropsDetail) {
                        launchSingleTop = true
                        popUpTo(Screen.Main.route)
                    }
                },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.CropsDetail.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            CropsDetailRoute(
                scope = appState.coroutineScope,
                onBack = { appState.popBackStack() },
                moveCropsInfo = { appState.navigate(Screen.CropsInfoDetail) },
                moveEditCrops = { appState.navigate(Screen.AddCrops) },
                moveDiaryList = { appState.navigate(Screen.DiaryList) },
                moveDiaryDetail = { appState.navigate(Screen.DiaryDetail) },
                moveAddDiary = { appState.navigate(Screen.AddDiary) },
                moveMain = {
                    appState.navigateWithOptions(Screen.Main) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                moveRecipeWeb = { appState.navigate(Screen.RecipeWeb) },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.RecipeWeb.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            RecipeWebRoute(
                onBack = { appState.popBackStack() }
            )
        }
        composable(
            route = Screen.DiaryList.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            DiaryListRoute(
                scope = appState.coroutineScope,
                moveDiary = { appState.navigate(Screen.DiaryDetail) },
                moveEditDiary = { appState.navigate(Screen.AddDiary) },
                onBack = { appState.popBackStack() },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.AddDiary.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AddDiaryRoute(
                    moveDiaryDetail = {
                      appState.navigateWithOptions(Screen.DiaryDetail) {
                          popUpTo(Screen.AddDiary.route) { inclusive = true }
                      }
                    },
                    onBack = { appState.popBackStack() },
                    onShowSnackBar = onShowSnackBar
                )
            }
        }
        composable(
            route = Screen.DiaryDetail.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            DiaryDetailRoute(
                scope = appState.coroutineScope,
                moveEditDiary = { appState.navigate(Screen.AddDiary) },
                onBack = { appState.popBackStack() },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.My.route,
            enterTransition = enterAnimation(),
            popEnterTransition = popEnterAnimation()
        ) {
            MyRoute(
                moveSetting = { appState.navigate(Screen.Setting) },
                moveChangeProfile = { appState.navigate(Screen.ChangeProfile) },
                moveChangeGarden = { appState.navigate(Screen.ChangeGarden) },
                onBack = { appState.popBackStack() }
            )
        }
        composable(
            route = Screen.ChangeProfile.route,
            enterTransition = enterAnimation(),
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ChangeProfileRoute(
                    onBack = { appState.popBackStack() },
                    onShowSnackBar = onShowSnackBar
                )
            }
        }
        composable(
            route = Screen.ChangeGarden.route,
            enterTransition = enterAnimation(),
        ) {
            ChangeGardenRoute(
                onBack = { appState.popBackStack() },
                onShowSnackBar = onShowSnackBar
            )
        }
        composable(
            route = Screen.Setting.route,
            enterTransition = enterAnimation(),
        ) {
            SettingRoute(
                scope = appState.coroutineScope,
                moveLogin = { appState.navigateTopLevelScreen(ScreenGraph.LoginGraph) },
                onBack = { appState.popBackStack() },
                onShowSnackBar = onShowSnackBar
            )
        }
    }
}