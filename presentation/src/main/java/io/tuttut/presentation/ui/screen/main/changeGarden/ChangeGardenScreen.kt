package io.tuttut.presentation.ui.screen.main.changeGarden

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.withScreenPadding

@Composable
fun ChangeGardenRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    ChangeGardenScreen(
        uiState = ChangeGardenUiState.Nothing,
        typedGardenName = "",
        typeGardenName = {},
        resetGardenName = { /*TODO*/ },
        onSubmit = { /*TODO*/ },
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun ChangeGardenScreen(
    modifier: Modifier = Modifier,
    uiState: ChangeGardenUiState,
    typedGardenName: String,
    typeGardenName: (String) -> Unit,
    resetGardenName: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = stringResource(id = R.string.change_garden_info),
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(screenHorizontalPadding)
        ) {
            TutTutLabel(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.profile_name)
            )
            TutTutTextField(
                value = typedGardenName,
                placeHolder = stringResource(id = R.string.garden_name_placeholder),
                supportingText = stringResource(id = R.string.text_limit),
                onValueChange = typeGardenName,
                onResetValue = resetGardenName
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            TutTutButton(
                text = stringResource(id = R.string.change),
                isLoading = uiState == ChangeGardenUiState.Loading,
                enabled = typedGardenName.trim().length in 1 .. 10,
                onClick = onSubmit
            )
        }
    }
}