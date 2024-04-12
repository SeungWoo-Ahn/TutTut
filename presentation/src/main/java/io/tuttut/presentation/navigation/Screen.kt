package io.tuttut.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Participate : Screen("participate")
    data object Welcome : Screen("welcome")
    data object Main : Screen("main")
    data object CropsDetail : Screen("cropsDetail")
    data object SelectCrops : Screen("selectCrops")
    data object CropsInfoDetail : Screen("cropsInfoDetail")
    data object AddCrops : Screen("addCrops")
    data object RecipeWeb : Screen("recipeWeb")
    data object DiaryList : Screen("diaryList")
    data object DiaryDetail : Screen("diaryDetail")
    data object AddDiary : Screen("addDiary")
    data object My : Screen("my")

    data object ChangeProfile : Screen("changeProfile")

    data object ChangeGarden : Screen("changeGarden")

    data object Setting : Screen("setting")

    data object Admin : Screen("admin")
}