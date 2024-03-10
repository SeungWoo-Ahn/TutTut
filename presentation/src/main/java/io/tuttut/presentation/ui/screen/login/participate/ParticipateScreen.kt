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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.ConfirmGardenDialog
import io.tuttut.presentation.ui.component.SupportingTextType
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutSelect
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar

@Composable
fun ParticipateRoute(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: ParticipateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val isNew by viewModel.isNew
    val typedName by viewModel.typedName
    val typedCode by viewModel.typedCode
    val searchedGarden by viewModel.authRepo.searchedGarden.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    ParticipateScreen(
        modifier = modifier,
        isLoading = uiState == ParticipateUiState.Loading,
        isNew = isNew,
        typedName = typedName,
        typedCode = typedCode,
        codeSupportingText = viewModel.codeSupportingText,
        codeSupportingTextType = viewModel.supportingTextType,
        typeName = viewModel::typeName,
        typeCode = viewModel::typeCode,
        resetName = viewModel::resetName,
        resetCode = viewModel::resetCode,
        changeIsNew = viewModel::changeIsNew,
        onNext = { viewModel.onNext({ keyboardController?.hide() }, onNext) },
        onBack = onBack
    )
    ConfirmGardenDialog(garden = searchedGarden, onDismissRequest = {}, onConfirm = {})
    BackHandler(onBack = onBack)
}

@Composable
internal fun ParticipateScreen(
    modifier: Modifier,
    isLoading: Boolean,
    isNew: Boolean,
    typedName: String,
    typedCode: String,
    codeSupportingText: String,
    codeSupportingTextType: SupportingTextType,
    typeName: (String) -> Unit,
    typeCode: (String) -> Unit,
    resetName: () -> Unit,
    resetCode: () -> Unit,
    changeIsNew: (Boolean) -> Unit,
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
            TutTutLabel(title = stringResource(id = R.string.participate_type))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TutTutSelect(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.participate_new),
                    isSelect = isNew
                ) { changeIsNew(true) }
                Spacer(modifier = Modifier.width(12.dp))
                TutTutSelect(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.participate_code),
                    isSelect = !isNew
                ) { changeIsNew(false) }
            }
            Spacer(modifier = Modifier.height(44.dp))
            if (isNew) {
                TutTutLabel(title = stringResource(id = R.string.garden_name), space = 0)
                TutTutTextField(
                    value = typedName,
                    placeHolder = stringResource(id = R.string.garden_name_placeholder),
                    supportingText = stringResource(id = R.string.text_limit),
                    onValueChange = typeName,
                    onResetValue = resetName
                )
            } else {
                TutTutLabel(title = stringResource(id = R.string.garden_code), space = 0)
                TutTutTextField(
                    value = typedCode,
                    placeHolder = stringResource(id = R.string.garden_code_placeholder),
                    supportingText = codeSupportingText,
                    supportingTextType = codeSupportingTextType,
                    onValueChange = typeCode,
                    onResetValue = resetCode
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            TutTutButton(
                text = stringResource(id = R.string.confirm),
                isLoading = isLoading,
                enabled = (isNew && typedName.isNotEmpty()) || (!isNew && typedCode.length == 6),
                onClick = onNext
            )
        }
    }
}