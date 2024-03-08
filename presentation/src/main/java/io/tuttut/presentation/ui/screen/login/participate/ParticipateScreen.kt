package io.tuttut.presentation.ui.screen.login.participate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar

@Composable
fun ParticipateRoute(modifier: Modifier = Modifier, onNext: () -> Unit, onBack: () -> Unit) {
    ParticipateScreen(modifier = modifier, onNext = onNext, onBack = onBack)
    BackHandler(onBack = onBack)
}

@Composable
fun ParticipateScreen(modifier: Modifier, onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier) {
        TutTutTopBar(title = stringResource(id = R.string.start_tuttut), onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .withScreenPadding()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TutTutTextField(
                placeHolder = stringResource(id = R.string.garden_name_placeholder),
                supportingText = stringResource(id = R.string.text_limit)
            )
            Spacer(modifier = Modifier.weight(1f))
            TutTutButton(
                text = stringResource(id = R.string.confirm),
                isLoading = false,
                onClick = onNext
            )
        }
    }
}