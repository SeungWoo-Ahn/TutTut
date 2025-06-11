package io.tuttut.presentation.ui.screen.main.navigation

import android.os.Build
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.presentation.navigation.MainScreen
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.ui.TutTutAppState
import io.tuttut.presentation.ui.screen.login.navigation.navigateToLoginGraph
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

fun NavGraphBuilder.addNestedMainGraph(
    appState: TutTutAppState,
    onShowSnackBar: suspend (String, String?) -> Boolean
) {
    val navController = appState.navController

    navigation<ScreenGraph.MainGraph>(startDestination = MainScreen.Main) {
        composable<MainScreen.Main> {
            MainRoute(
                moveSelectCrops = navController::navigateToSelectCrops,
                moveMy = navController::navigateToMy,
                moveDetail = navController::navigateToCropsDetail
            )
        }
        composable<MainScreen.CropsDetail> {
            CropsDetailRoute(
                scope = appState.coroutineScope,
                moveCropsInfo = navController::navigateToCropsInfoDetail,
                moveEditCrops = navController::navigateToAddCrops,
                moveDiaryList = navController::navigateToDiaryList,
                moveDiaryDetail = navController::navigateToDiaryDetail,
                moveAddDiary = navController::navigateToAddDiary,
                moveMain = navController::navigateToMain,
                moveRecipeWeb = navController::navigateToRecipeWeb,
                onBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar
            )
        }
        composable<MainScreen.SelectCrops> {
            SelectCropsRoute(
                moveDetail = navController::navigateToCropsInfoDetail,
                moveAdd = navController::navigateToAddCrops,
                onBack = navController::popBackStack,
            )
        }
        composable<MainScreen.CropsInfoDetail> {
            CropsInfoDetailRoute(
                moveAdd = navController::navigateToAddCrops,
                moveRecipeWeb = navController::navigateToRecipeWeb,
                onBack = navController::popBackStack,
            )
        }
        composable<MainScreen.AddCrops> {
            AddCropsRoute(
                scope = appState.coroutineScope,
                moveCropsDetail = { cropsId ->
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(MainScreen.Main, inclusive = false)
                        .setLaunchSingleTop(true)
                        .build()
                    navController.navigateToCropsDetail(cropsId, navOptions)
                },
                onBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar
            )
        }
        composable<MainScreen.RecipeWeb> {
            RecipeWebRoute(
                onBack = navController::popBackStack
            )
        }
        composable<MainScreen.DiaryList> {
            DiaryListRoute(
                scope = appState.coroutineScope,
                moveDiary = navController::navigateToDiaryDetail,
                moveEditDiary = navController::navigateToAddDiary,
                onBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar
            )
        }
        composable<MainScreen.DiaryDetail> {
            DiaryDetailRoute(
                scope = appState.coroutineScope,
                moveEditDiary = navController::navigateToAddDiary,
                onBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar
            )
        }
        composable<MainScreen.AddDiary> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AddDiaryRoute(
                    moveDiaryDetail = { diaryId ->
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(MainScreen.AddDiary, inclusive = true)
                            .build()
                        navController.navigateToDiaryDetail(diaryId, navOptions)
                    },
                    onBack = navController::popBackStack,
                    onShowSnackBar = onShowSnackBar
                )
            }
        }
        composable<MainScreen.My> {
            MyRoute(
                moveSetting = navController::navigateToSetting,
                moveChangeProfile = navController::navigateToChangeProfile,
                moveChangeGarden = navController::navigateToChangeGarden,
                onBack = navController::popBackStack
            )
        }
        composable<MainScreen.ChangeProfile> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ChangeProfileRoute(
                    onBack = navController::popBackStack,
                    onShowSnackBar = onShowSnackBar
                )
            }
        }
        composable<MainScreen.ChangeGarden> {
            ChangeGardenRoute(
                onBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar
            )
        }
        composable<MainScreen.Setting> {
            SettingRoute(
                scope = appState.coroutineScope,
                moveLogin = navController::navigateToLoginGraph,
                onBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar
            )
        }
    }
}

fun NavController.navigateToMainGraph() = navigate(ScreenGraph.MainGraph) {
    popUpTo(graph.id) { inclusive = true }
}

private fun NavController.navigateToMain() = navigate(MainScreen.Main) {
    popUpTo(graph.id) { inclusive = true }
}
private fun NavController.navigateToCropsDetail(cropsId: String, navOptions: NavOptions? = null) =
    navigate(MainScreen.CropsDetail(cropsId), navOptions)
private fun NavController.navigateToSelectCrops() = navigate(MainScreen.SelectCrops)
private fun NavController.navigateToCropsInfoDetail(key: CropsKey) = navigate(MainScreen.CropsInfoDetail(key))
private fun NavController.navigateToAddCrops(cropsId: String? = null) = navigate(MainScreen.AddCrops(cropsId))
private fun NavController.navigateToRecipeWeb(name: String, link: String) = navigate(MainScreen.RecipeWeb(name, link))
private fun NavController.navigateToDiaryList(cropsId: String) = navigate(MainScreen.DiaryList(cropsId))
private fun NavController.navigateToDiaryDetail(diaryId: String, navOptions: NavOptions? = null) =
    navigate(MainScreen.DiaryDetail(diaryId), navOptions)
private fun NavController.navigateToAddDiary(diaryId: String? = null) = navigate(MainScreen.AddDiary(diaryId))
private fun NavController.navigateToMy() = navigate(MainScreen.My)
private fun NavController.navigateToChangeProfile() = navigate(MainScreen.ChangeProfile)
private fun NavController.navigateToChangeGarden() = navigate(MainScreen.ChangeGarden)
private fun NavController.navigateToSetting() = navigate(MainScreen.Setting)