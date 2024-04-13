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
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.TextButton
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.withScreenPadding

@Composable
fun SettingRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    SettingScreen(
        modifier = modifier,
        quitGarden = {  },
        signOut = { /*TODO*/ },
        withDraw = { /*TODO*/ },
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun SettingScreen(
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
            TutTutLabel(title = stringResource(id = R.string.garden_setting), space = 10)
            TextButton(text = stringResource(id = R.string.sign_out), onClick = signOut)
            TextButton(text = stringResource(id = R.string.withdraw), onClick = withDraw)
        }
    }
}