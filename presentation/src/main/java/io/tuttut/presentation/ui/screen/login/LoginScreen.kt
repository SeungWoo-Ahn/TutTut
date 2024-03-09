package io.tuttut.presentation.ui.screen.login

import android.app.Activity.RESULT_OK
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.GoogleLoginButton
import io.tuttut.presentation.util.GoogleAuthClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    googleAuthClient: GoogleAuthClient,
    onNext: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = googleAuthClient.signInWithIntent(result.data ?: return@launch)
                }
            }
        }
    )
    LoginScreen(modifier = modifier) {
        viewModel.onLogin {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                coroutineScope.launch {
                    val signInIntentSender = googleAuthClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        }
    }
}

@Composable
internal fun LoginScreen(modifier: Modifier, onLogin: () -> Unit) {
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
        GoogleLoginButton(isLoading = false, onLogin = onLogin)
    }
}