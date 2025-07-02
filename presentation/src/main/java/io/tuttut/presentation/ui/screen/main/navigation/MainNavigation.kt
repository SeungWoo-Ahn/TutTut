package io.tuttut.presentation.ui.screen.main.navigation

import android.os.Build
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.presentation.navigation.MainScreen
import io.tuttut.presentation.navigation.ScreenGraph
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
        composable<MainScreen.CropsDetail> { backStackEntry ->
            val cropsId = backStackEntry.toRoute<MainScreen.CropsDetail>().cropsId
            CropsDetailRoute(
                scope = appState.coroutineScope,
                moveCropsInfo = { key, keyword -> navController.navigateToCropsInfoDetail(key, keyword,true) },
                moveEditCrops = { navController.navigateToAddCrops(MainScreen.AddCrops.Purpose.ForEdit(cropsId)) },
                moveDiaryList = { cropsName -> navController.navigateToDiaryList(cropsId, cropsName) },
                moveDiaryDetail = navController::navigateToDiaryDetail,
                moveAddDiary = navController::navigateToAddDiary,
                moveMain = navController::navigateToMain,
                moveRecipeWeb = navController::navigateToRecipeWeb,
                onBack = navController::popBackStack,
            )
        }
        composable<MainScreen.SelectCrops> {
            SelectCropsRoute(
                moveDetail = { key, keyword -> navController.navigateToCropsInfoDetail(key, keyword, false) },
                moveAdd = { navController.navigateToAddCrops(MainScreen.AddCrops.Purpose.ForAdd(CropsKey.CUSTOM)) },
                onBack = navController::popBackStack,
            )
        }
        composable<MainScreen.CropsInfoDetail> {
            CropsInfoDetailRoute(
                moveAdd = { key -> navController.navigateToAddCrops(MainScreen.AddCrops.Purpose.ForAdd(key)) },
                moveRecipeWeb = navController::navigateToRecipeWeb,
                onBack = navController::popBackStack,
            )
        }
        composable<MainScreen.AddCrops> {
            AddCropsRoute(
                scope = appState.coroutineScope,
                moveCropsDetail = { cropsId, cropsName ->
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(MainScreen.Main, inclusive = false)
                        .setLaunchSingleTop(true)
                        .build()
                    navController.navigateToCropsDetail(cropsId, cropsName, navOptions)
                },
                onBack = navController::popBackStack,
            )
        }
        composable<MainScreen.RecipeWeb> { backStackEntry ->
            val (name, link) = backStackEntry.toRoute<MainScreen.RecipeWeb>()
            RecipeWebRoute(
                cropsName = name,
                link = link,
                onBack = navController::popBackStack
            )
        }
        composable<MainScreen.DiaryList> { backStackEntry ->
            val cropsName = backStackEntry.toRoute<MainScreen.DiaryList>().cropsName
            DiaryListRoute(
                scope = appState.coroutineScope,
                cropsName = cropsName,
                moveDiary = navController::navigateToDiaryDetail,
                moveEditDiary = navController::navigateToAddDiary,
                onBack = navController::popBackStack,
            )
        }
        composable<MainScreen.DiaryDetail> { backStackEntry ->
            val diaryId = backStackEntry.toRoute<MainScreen.DiaryDetail>().diaryId

            DiaryDetailRoute(
                scope = appState.coroutineScope,
                moveEditDiary = { navController.navigateToAddDiary(diaryId) },
                onBack = navController::popBackStack,
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
            )
        }
        composable<MainScreen.Setting> {
            SettingRoute(
                scope = appState.coroutineScope,
                onBack = navController::popBackStack,
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

private fun NavController.navigateToCropsDetail(cropsId: String, cropsName: String, navOptions: NavOptions? = null) =
    navigate(MainScreen.CropsDetail(cropsId, cropsName), navOptions)

private fun NavController.navigateToSelectCrops() =
    navigate(MainScreen.SelectCrops)

private fun NavController.navigateToCropsInfoDetail(key: CropsKey, keyword: String, readOnly: Boolean) =
    navigate(MainScreen.CropsInfoDetail(key, keyword, readOnly))

private fun NavController.navigateToAddCrops(purpose: MainScreen.AddCrops.Purpose) =
    navigate(MainScreen.AddCrops(purpose))

private fun NavController.navigateToRecipeWeb(name: String, link: String) =
    navigate(MainScreen.RecipeWeb(name, link))

private fun NavController.navigateToDiaryList(cropsId: String, cropsName: String) =
    navigate(MainScreen.DiaryList(cropsId, cropsName))

private fun NavController.navigateToDiaryDetail(diaryId: String, navOptions: NavOptions? = null) =
    navigate(MainScreen.DiaryDetail(diaryId), navOptions)

private fun NavController.navigateToAddDiary(diaryId: String? = null) =
    navigate(MainScreen.AddDiary(diaryId))

private fun NavController.navigateToMy() =
    navigate(MainScreen.My)

private fun NavController.navigateToChangeProfile() =
    navigate(MainScreen.ChangeProfile)

private fun NavController.navigateToChangeGarden() =
    navigate(MainScreen.ChangeGarden)

private fun NavController.navigateToSetting() =
    navigate(MainScreen.Setting)