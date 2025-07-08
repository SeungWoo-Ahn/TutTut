package io.tuttut.presentation.navigation

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.usecase.cropsInfo.GetCropsInfoListUseCase
import io.tuttut.domain.usecase.user.GetCredentialFlowUseCase
import io.tuttut.domain.usecase.user.GetGardenUserListUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.GoogleAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getGardenUserListUseCase: GetGardenUserListUseCase,
    private val getCropsInfoListUseCase: GetCropsInfoListUseCase,
    private val googleAuth: GoogleAuth,
    getCredentialFlowUseCase: GetCredentialFlowUseCase,
) : BaseViewModel() {
    val authFlow: Flow<Boolean> =
        getCredentialFlowUseCase()
            .map { true }
            .catch { emit(false) }

    suspend fun login(context: Context) = googleAuth.login(context)

    suspend fun cacheBasicData() {
        withContext(Dispatchers.IO) {
            getGardenUserListUseCase()
            getCropsInfoListUseCase()
        }
    }
}