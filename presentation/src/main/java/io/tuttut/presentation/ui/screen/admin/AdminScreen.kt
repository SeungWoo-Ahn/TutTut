package io.tuttut.presentation.ui.screen.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutTopBar

@Composable
fun AdminRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    AdminScreen(
        modifier = modifier,
        onClick = viewModel::onClick,
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun AdminScreen(
    modifier: Modifier,
    onClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = stringResource(id = R.string.admin_add_crops_info),
            needBack = true,
            onBack = onBack
        )
        Column(modifier = Modifier.weight(1f)) {

        }
        TutTutButton(
            text = stringResource(id = R.string.add),
            isLoading = false,
            onClick = onClick
        )
    }
}