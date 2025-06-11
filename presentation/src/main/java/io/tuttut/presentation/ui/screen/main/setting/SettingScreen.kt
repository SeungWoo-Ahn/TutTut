package io.tuttut.presentation.ui.screen.main.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.NegativeBottomSheet
import io.tuttut.presentation.ui.component.TextButton
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.withScreenPadding
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    moveLogin: () -> Unit,
    onBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: SettingViewModel = hiltViewModel()
) {
    SettingScreen(
        modifier = modifier,
        quitGarden = { viewModel.showQuitSheet = true },
        withDraw = { viewModel.showWithdrawSheet = true },
        signOut = { viewModel.signOut(moveLogin, onShowSnackBar) },
        onBack = onBack
    )
    NegativeBottomSheet(
        showSheet = viewModel.showQuitSheet,
        title = stringResource(id = R.string.quit_warning),
        buttonText = stringResource(id = R.string.quit_garden),
        scope = scope,
        onButton = { viewModel.quitGarden(moveLogin, onShowSnackBar) },
        onDismissRequest = { viewModel.showQuitSheet = false }
    )
    NegativeBottomSheet(
        showSheet = viewModel.showWithdrawSheet,
        title = stringResource(id = R.string.withdraw_warning),
        buttonText = stringResource(id = R.string.withdraw),
        scope = scope,
        onButton = { viewModel.withDraw(moveLogin, onShowSnackBar) },
        onDismissRequest = { viewModel.showWithdrawSheet = false }
    )
    BackHandler(onBack = onBack)
}

@Composable
private fun SettingScreen(
    modifier: Modifier,
    quitGarden: () -> Unit,
    signOut: () -> Unit,
    withDraw: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = stringResource(id = R.string.setting),
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .withScreenPadding()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TutTutLabel(title = stringResource(id = R.string.garden_setting), space = 10)
            TextButton(text = stringResource(id = R.string.quit_garden), onClick = quitGarden)
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
            Spacer(modifier = Modifier.height(20.dp))
            TutTutLabel(title = stringResource(id = R.string.account_setting), space = 10)
            TextButton(text = stringResource(id = R.string.sign_out), onClick = signOut)
            TextButton(text = stringResource(id = R.string.withdraw), onClick = withDraw)
        }
    }
}