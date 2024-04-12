package io.tuttut.presentation.ui.screen.login.welcome

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.tuttut.presentation.R
import io.tuttut.presentation.util.withScreenPadding
import io.tuttut.presentation.ui.component.TutTutButton

@Composable
fun WelcomeRoute(modifier: Modifier = Modifier, onNext: () -> Unit) {
    WelcomeScreen(modifier = modifier, onNext = onNext)
    BackHandler(onBack = onNext)
}

@Composable
internal fun WelcomeScreen(modifier: Modifier, onNext: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .withScreenPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(130.dp))
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "welcome-image"
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.welcome),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 30.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        TutTutButton(
            text = stringResource(id = R.string.start),
            isLoading = false,
            onClick = onNext
        )
    }
}