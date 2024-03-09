package io.tuttut.presentation.ui.screen.login.participate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutSelect
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar

@Composable
fun ParticipateRoute(modifier: Modifier = Modifier, onNext: () -> Unit, onBack: () -> Unit) {
    ParticipateScreen(modifier = modifier, onNext = onNext, onBack = onBack)
    BackHandler(onBack = onBack)
}

@Composable
fun ParticipateScreen(modifier: Modifier, onNext: () -> Unit, onBack: () -> Unit) {
    var isNew by remember { mutableStateOf(true) }
    Column(modifier) {
        TutTutTopBar(title = stringResource(id = R.string.start_tuttut), onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .withScreenPadding()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TutTutLabel(title = stringResource(id = R.string.participate_type))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TutTutSelect(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.participate_new),
                    isSelect = isNew
                ) { isNew = true }
                Spacer(modifier = Modifier.width(12.dp))
                TutTutSelect(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.participate_code),
                    isSelect = !isNew
                ) { isNew = false }
            }
            Spacer(modifier = Modifier.height(44.dp))
            if (isNew) {
                TutTutLabel(title = stringResource(id = R.string.garden_name), space = 0)
                TutTutTextField(
                    placeHolder = stringResource(id = R.string.garden_name_placeholder),
                    supportingText = stringResource(id = R.string.text_limit)
                )
            } else {
                TutTutLabel(title = stringResource(id = R.string.garden_code), space = 0)
                TutTutTextField(placeHolder = stringResource(id = R.string.garden_code))
            }
            Spacer(modifier = Modifier.weight(1f))
            TutTutButton(
                text = stringResource(id = R.string.confirm),
                isLoading = false,
                onClick = onNext
            )
        }
    }
}