package io.tuttut.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.tuttut.data.model.dto.Garden
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding

@Composable
fun TutTutDialog(
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = dismissOnClickOutside,
            dismissOnBackPress = dismissOnBackPress,
            usePlatformDefaultWidth = false
        ),
        content = content
    )
}

@Composable
fun ConfirmGardenDialog(
    modifier: Modifier = Modifier,
    garden: Garden?,
    isLoading: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    TutTutDialog(
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = modifier.fillMaxWidth().withScreenPadding(),
            color = MaterialTheme.colorScheme.inverseSurface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Text(text = stringResource(id = R.string.confirm_garden_title), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "${garden?.name} #${garden?.code}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    TutTutButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.close),
                        isLoading = false,
                        buttonColor = MaterialTheme.colorScheme.inverseOnSurface,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onDismissRequest
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    TutTutButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.participate),
                        isLoading = isLoading,
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

