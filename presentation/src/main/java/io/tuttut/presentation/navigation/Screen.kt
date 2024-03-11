package io.tuttut.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val navArgument: List<NamedNavArgument> = emptyList()) {
    data object Login : Screen("login")
    data object Participate : Screen("participate")
    data object Welcome : Screen("welcome")
    data object Main : Screen("main")
    data object CropsDetail : Screen(
        "cropsDetail/{cropsId}",
        listOf(navArgument("corpsId") { type = NavType.StringType })
    ) {
        fun createRoute(corpsId: String) = "cropsDetail/${corpsId}"
    }
    data object SelectCrops : Screen("selectCrops")
    data object CropsInfoDetail : Screen(
        "selectCrops/{cropsId}",
        listOf(navArgument("cropsId") { type = NavType.StringType })
    ) {
        fun createRoute(cropsId: String) = "selectCrops/${cropsId}"
    }
    data object AddCrops : Screen("addCrops")
    data object DiaryDetail : Screen(
        "diaryDetail/{diaryId}",
        listOf(navArgument("diaryId") { type = NavType.StringType })
    ) {
        fun createRoute(diaryId: String) = "diaryDetail/${diaryId}"
    }
    data object AddDiary : Screen("addDiary")
    data object My : Screen("my")
}