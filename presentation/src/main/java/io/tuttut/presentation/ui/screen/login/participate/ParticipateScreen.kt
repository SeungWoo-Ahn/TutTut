package io.tuttut.presentation.ui.screen.login.participate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.presentation.R
import io.tuttut.presentation.util.withScreenPadding
import io.tuttut.presentation.ui.component.ConfirmGardenDialog
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutSelect
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.state.CodeTextFieldState
import io.tuttut.presentation.ui.state.ITextFieldState

@Composable
fun ParticipateRoute(
    modifier: Modifier = Modifier,
    moveWelcome: () -> Unit,
    onBack: () -> Unit,
    viewModel: ParticipateViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val tab = viewModel.tab
    val buttonEnabled by remember { derivedStateOf { viewModel.validate() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    ParticipateScreen(
        modifier = modifier,
        isLoading = uiState == ParticipateUiState.Loading,
        buttonEnabled = buttonEnabled,
        tab = tab,
        nameState = viewModel.nameState,
        codeState = viewModel.codeState,
        onTabChanged = viewModel::onTabChanged,
        onNext = {
            keyboardController?.hide()
            viewModel.onNext(moveWelcome)
        },
        onBack = onBack
    )
    ConfirmGardenDialog(
        uiState = uiState,
        onDismissRequest = viewModel::resetUiState,
        onConfirm = { garden -> viewModel.joinGarden(garden, moveWelcome) }
    )
    BackHandler(onBack = onBack)
}

@Composable
private fun ParticipateScreen(
    modifier: Modifier,
    isLoading: Boolean,
    buttonEnabled: Boolean,
    tab: ParticipateTab,
    nameState: ITextFieldState,
    codeState: CodeTextFieldState,
    onTabChanged: (ParticipateTab) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier) {
        TutTutTopBar(
            title = stringResource(id = R.string.start_tuttut),
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .withScreenPadding()
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TutTutLabel(title = stringResource(id = R.string.participate_type), space = 16)
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TutTutSelect(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.participate_new),
                    isSelect = tab == ParticipateTab.CREATE,
                    onSelect = { onTabChanged(ParticipateTab.CREATE) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                TutTutSelect(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.participate_code),
                    isSelect = tab == ParticipateTab.JOIN,
                    onSelect = { onTabChanged(ParticipateTab.JOIN) }
                )
            }
            Spacer(modifier = Modifier.height(44.dp))
            when (tab) {
                ParticipateTab.CREATE -> {
                    TutTutLabel(title = stringResource(id = R.string.garden_name))
                    TutTutTextField(
                        state = nameState,
                        placeHolder = stringResource(id = R.string.garden_name_placeholder),
                    )
                }
                ParticipateTab.JOIN -> {
                    TutTutLabel(title = stringResource(id = R.string.garden_code))
                    TutTutTextField(
                        state = codeState,
                        placeHolder = stringResource(id = R.string.garden_code_placeholder),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            TutTutButton(
                text = stringResource(id = R.string.confirm),
                isLoading = isLoading,
                enabled = buttonEnabled,
                onClick = onNext
            )
        }
    }
}