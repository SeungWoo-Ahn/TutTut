package io.tuttut.presentation.navigation

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.usecase.user.GetCredentialFlowUseCase
import io.tuttut.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    getCredentialFlowUseCase: GetCredentialFlowUseCase,
) : BaseViewModel() {
    val authFlow: Flow<Boolean> =
        getCredentialFlowUseCase()
            .map { true }
            .catch { emit(false) }
}