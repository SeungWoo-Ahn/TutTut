package io.tuttut.presentation.ui.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.GoogleLoginButton
import io.tuttut.presentation.ui.component.PolicyBottomSheet
import io.tuttut.presentation.util.withScreenPadding

private const val SERVICE_POLICY_URL = "https://melodious-homegrown-e4d.notion.site/1e823e4b62634dcab576e05de7bb91cd"

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    moveParticipate: (JoinRequest) -> Unit,
    moveMain: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current
    LoginScreen(
        modifier = modifier,
        isLoading = uiState == LoginUiState.Loading,
        onLogin = { viewModel.onLogin(context) }
    )
    PolicyBottomSheet(
        uiState = uiState,
        togglePolicyChecked = viewModel::togglePolicyChecked,
        togglePersonalChecked = viewModel::togglePersonalChecked,
        showPolicy = { viewModel.openBrowser(context, SERVICE_POLICY_URL) },
        showPersonal = { viewModel.openBrowser(context, SERVICE_POLICY_URL) },
        onAgreement = viewModel::join,
        onDismissRequest = viewModel::resetUiState
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier,
    isLoading: Boolean,
    onLogin: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .withScreenPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(188.dp))
        Text(text = stringResource(id = R.string.app_kor_name), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(id = R.string.catchphrase), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        GoogleLoginButton(isLoading = isLoading, onLogin = onLogin)
    }
}