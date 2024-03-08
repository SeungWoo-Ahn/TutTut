package io.tuttut.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.buttonHeight

@Composable
fun GoogleLoginButton(isLoading: Boolean, onLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(start = 14.dp)
            .clickable { if (!isLoading) onLogin() },
    ) {
        Image(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(id = R.drawable.logo_google),
            contentDescription = "google-logo",
        )
        if (!isLoading) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.google_login),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelMedium
            )
        } else {
            TutTutLoading(
                modifier = Modifier.align(Alignment.Center),
                size = 18,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TutTutButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
            disabledContentColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onClick
    ) {
        if (!isLoading) {
            Text(
                text = text,
                color = if (enabled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.headlineMedium
            )
        } else {
            TutTutLoading(size = 18)
        }
    }
}

@Preview
@Composable
fun PreviewGoogleLoginButton() {
    GoogleLoginButton(isLoading = false) {}
}

@Preview
@Composable
fun PreviewTutTutButton() {
    TutTutButton(text = "완료", isLoading = false) {

    }
}