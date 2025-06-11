package io.tuttut.presentation.navigation

import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.user.JoinRequest
import kotlinx.serialization.Serializable

sealed interface LoginScreen {
    @Serializable
    data object Login : LoginScreen

    @Serializable
    data class Participate(val joinRequest: JoinRequest) : LoginScreen

    @Serializable
    data object Welcome : LoginScreen
}

sealed interface MainScreen {
    @Serializable
    data object Main : MainScreen

    @Serializable
    data class CropsDetail(val cropsId: String) : MainScreen

    @Serializable
    data object SelectCrops : MainScreen

    @Serializable
    data class CropsInfoDetail(val key: CropsKey) : MainScreen

    @Serializable
    data class AddCrops(val cropsId: String?) : MainScreen

    @Serializable
    data class RecipeWeb(val name: String, val link: String) : MainScreen

    @Serializable
    data class DiaryList(val cropsId: String) : MainScreen

    @Serializable
    data class DiaryDetail(val diaryId: String) : MainScreen

    @Serializable
    data class AddDiary(val diaryId: String?) : MainScreen

    @Serializable
    data object My : MainScreen

    @Serializable
    data object ChangeProfile : MainScreen

    @Serializable
    data object ChangeGarden : MainScreen

    @Serializable
    data object Setting : MainScreen
}