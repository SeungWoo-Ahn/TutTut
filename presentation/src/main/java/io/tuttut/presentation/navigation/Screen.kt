package io.tuttut.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val navArgument: List<NamedNavArgument> = emptyList()) {
    data object Login : Screen("login")
    data object Participate : Screen("participate")
    data object Welcome : Screen("welcome")
    data object Main : Screen("main")
    data object PlantDetail : Screen(
        "plantDetail/{plantId}",
        listOf(navArgument("plantId") { type = NavType.StringType })
    ) {
        fun createRoute(plantId: String) = "plantDetail/${plantId}"
    }
    data object RecommendPlant : Screen("recommendPlant")
    data object SelectRecommendPlant : Screen(
        "recommendPlant/{recommendId}",
        listOf(navArgument("recommendId") { type = NavType.StringType })
    ) {
        fun createRoute(recommendId: String) = "recommendPlant/${recommendId}"
    }
    data object AddPlant : Screen("addPlant")
    data object DiaryDetail : Screen(
        "diaryDetail/{diaryId}",
        listOf(navArgument("diaryId") { type = NavType.StringType })
    ) {
        fun createRoute(diaryId: String) = "diaryDetail/${diaryId}"
    }
    data object AddDiary : Screen("addDiary")
    data object My : Screen("my")
}