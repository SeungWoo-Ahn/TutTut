package io.tuttut.presentation.ui.screen.login

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
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.GoogleLoginButton

@Composable
fun LoginRoute(modifier: Modifier = Modifier) {
    LoginScreen(modifier = modifier)
}

@Composable
internal fun LoginScreen(modifier: Modifier) {
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
        GoogleLoginButton(isLoading = false) {}
    }
}