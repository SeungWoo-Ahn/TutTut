package io.tuttut.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val argsKey: String = "", val navArgument: List<NamedNavArgument> = emptyList()) {
    data object Login : Screen("login")
    data object Participate : Screen("participate")
    data object Welcome : Screen("welcome")
    data object Main : Screen("main")
    data object CropsDetail : Screen(
        route = "cropsDetail/{cropsId}",
        navArgument =  listOf(navArgument("cropsId") { type = NavType.StringType })
    ) {
        fun createRoute(cropsId: String) = "cropsDetail/${cropsId}"
    }
    data object SelectCrops : Screen("selectCrops")
    data object CropsInfoDetail : Screen(route = "cropsInfoDetail")
    data object AddCrops : Screen("addCrops")
    data object DiaryDetail : Screen(
        route = "diaryDetail/{diaryId}",
        navArgument = listOf(navArgument("diaryId") { type = NavType.StringType })
    ) {
        fun createRoute(diaryId: String) = "diaryDetail/${diaryId}"
    }
    data object AddDiary : Screen("addDiary")
    data object My : Screen("my")
}