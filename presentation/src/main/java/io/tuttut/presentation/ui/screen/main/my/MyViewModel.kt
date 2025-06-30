package io.tuttut.presentation.ui.screen.main.my

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.usecase.garden.GetGardenWithMemberUseCase
import io.tuttut.domain.usecase.user.GetCurrentUserUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.toUiModel
import io.tuttut.presentation.util.LinkUtil
import io.tuttut.presentation.util.ShareGardenData
import io.tuttut.presentation.util.ShareUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getGardenWithMemberUseCase: GetGardenWithMemberUseCase,
    private val shareUtil: ShareUtil,
    private val linkUtil: LinkUtil,
) : BaseViewModel() {
    var uiState by mutableStateOf<MyUiState>(MyUiState.Loading)
        private set

    init {
        viewModelScope.launch {
            getInitData().onSuccess { uiState = it }
        }
    }

    private suspend fun getInitData(): Result<MyUiState.Success> = runCatching {
        val user = getCurrentUserUseCase().getOrThrow().toUiModel()
        val garden = getGardenWithMemberUseCase().getOrThrow().toUiModel()
        MyUiState.Success(user, garden)
    }

    fun shareGarden(context: Context, data: ShareGardenData) = shareUtil.shareGarden(context, data)

    fun openBrowser(context: Context, url: String) = linkUtil.openBrowser(context, url)
}