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
import io.tuttut.presentation.ui.state.IEditTextState

@Composable
fun ParticipateRoute(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: ParticipateViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    ParticipateScreen(
        modifier = modifier,
        isLoading = viewModel.uiState == ParticipateUiState.Loading,
        tabState = viewModel.tabState,
        nameState = viewModel.nameState,
        codeState = viewModel.codeState,
        onNext = { viewModel.onNext({ keyboardController?.hide() }, onNext, onShowSnackBar) },
        onBack = onBack
    )
    ConfirmGardenDialog(
        showDialog = viewModel.dialogState.isOpen,
        isLoading = viewModel.dialogState.isLoading,
        garden = viewModel.dialogState.garden,
        onDismissRequest = viewModel.dialogState::dismiss,
        onConfirm = { viewModel.joinGarden(onNext, onShowSnackBar) }
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun ParticipateScreen(
    modifier: Modifier,
    isLoading: Boolean,
    tabState: ParticipateTabState,
    nameState: IEditTextState,
    codeState: ParticipateCodeState,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier) {
        TutTutTopBar(title = stringResource(id = R.string.start_tuttut), onBack = onBack)
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
                    isSelect = tabState.isNew
                ) { tabState.changeTab(true) }
                Spacer(modifier = Modifier.width(12.dp))
                TutTutSelect(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.participate_code),
                    isSelect = !tabState.isNew
                ) { tabState.changeTab(false) }
            }
            Spacer(modifier = Modifier.height(44.dp))
            if (tabState.isNew) {
                TutTutLabel(title = stringResource(id = R.string.garden_name))
                TutTutTextField(
                    value = nameState.typedText,
                    placeHolder = stringResource(id = R.string.garden_name_placeholder),
                    supportingText = stringResource(id = R.string.text_limit),
                    onValueChange = nameState::typeText,
                    onResetValue = nameState::resetText
                )
            } else {
                TutTutLabel(title = stringResource(id = R.string.garden_code))
                TutTutTextField(
                    value = codeState.typedText,
                    placeHolder = stringResource(id = R.string.garden_code_placeholder),
                    supportingText = codeState.supportingText,
                    supportingTextType = codeState.supportingTextType,
                    onValueChange = codeState::typeText,
                    onResetValue = codeState::resetText
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            TutTutButton(
                text = stringResource(id = R.string.confirm),
                isLoading = isLoading,
                enabled = (tabState.isNew && nameState.isValidate()) || (!tabState.isNew && codeState.isValidate()),
                onClick = onNext
            )
        }
    }
}