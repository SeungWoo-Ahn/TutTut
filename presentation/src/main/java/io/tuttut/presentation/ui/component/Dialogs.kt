package io.tuttut.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.tuttut.data.model.dto.Garden
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.util.withScreenPadding
import io.tuttut.presentation.util.convertMillisToDate
import io.tuttut.presentation.util.getDateLong
import io.tuttut.presentation.util.getDatePickerYearRange

@Composable
fun TutTutDialog(
    showDialog: Boolean,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    if (showDialog) {
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
}

@Composable
fun ConfirmGardenDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    isLoading: Boolean,
    garden: Garden,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    TutTutDialog(
        showDialog = showDialog,
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .withScreenPadding(),
            color = MaterialTheme.colorScheme.inverseSurface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Text(text = stringResource(id = R.string.confirm_garden_title), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "${garden.name} #${garden.code}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutTutDatePickerDialog(
    showDialog: Boolean,
    plantingDate: String,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val datePickerState = rememberDatePickerState(yearRange = getDatePickerYearRange()).apply {
        selectedDateMillis = getDateLong(plantingDate)
    }
    val selectedDate = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) } ?: ""
    if (showDialog) {
        DatePickerDialog(
            modifier = Modifier.padding(screenHorizontalPadding),
            onDismissRequest = onDismissRequest,
            confirmButton = { DatePickerButton { onDateSelected(selectedDate) } },
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false,
            )
        }
    }
}

