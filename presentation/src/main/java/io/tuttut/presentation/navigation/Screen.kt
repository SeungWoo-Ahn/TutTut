package io.tuttut.presentation.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(val route: String, val navArgument: List<NamedNavArgument> = emptyList()) {
    data object Login : Screen("login")
    data object Participate : Screen("participate")
    data object Welcome : Screen("welcome")
    data object Main : Screen("main")
    data object CropsDetail : Screen("cropsDetail")
    data object SelectCrops : Screen("selectCrops")
    data object CropsInfoDetail : Screen("cropsInfoDetail")
    data object AddCrops : Screen("addCrops")
    data object RecipeWeb : Screen("recipeWeb")
    data object DiaryDetail : Screen("diaryDetail")
    data object AddDiary : Screen("addDiary")
    data object My : Screen("my")
    data object Admin : Screen("admin")
}