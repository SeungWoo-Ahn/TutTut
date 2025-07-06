package io.tuttut.presentation.navigation

import io.tuttut.domain.model.cropsInfo.CropsKey
import kotlinx.serialization.Serializable

sealed interface LoginScreen {
    @Serializable
    data object Login : LoginScreen

    @Serializable
    data class Participate(val userId: String) : LoginScreen

    @Serializable
    data object Welcome : LoginScreen
}

sealed interface MainScreen {
    @Serializable
    data object Main : MainScreen

    @Serializable
    data class CropsDetail(val cropsId: String, val cropsName: String) : MainScreen

    @Serializable
    data object SelectCrops : MainScreen

    @Serializable
    data class CropsInfoDetail(
        val key: CropsKey,
        val keyword: String,
        val readOnly: Boolean
    ) : MainScreen

    @Serializable
    data class AddCrops(val cropsId: String?, val cropsKey: CropsKey?) : MainScreen

    @Serializable
    data class RecipeWeb(val name: String, val link: String) : MainScreen

    @Serializable
    data class DiaryList(val cropsId: String, val cropsName: String) : MainScreen

    @Serializable
    data class DiaryDetail(val diaryId: String) : MainScreen

    @Serializable
    data class AddDiary(val cropsId: String?, val diaryId: String?) : MainScreen

    @Serializable
    data object My : MainScreen

    @Serializable
    data object ChangeProfile : MainScreen

    @Serializable
    data object ChangeGarden : MainScreen

    @Serializable
    data object Setting : MainScreen
}