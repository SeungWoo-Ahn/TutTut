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
import io.tuttut.domain.model.garden.Garden
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.screen.login.participate.ParticipateUiState
import io.tuttut.presentation.util.withScreenPadding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
    uiState: ParticipateUiState,
    onDismissRequest: () -> Unit,
    onConfirm: (Garden) -> Unit
) {
    TutTutDialog(
        showDialog = uiState is ParticipateUiState.DialogState,
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        onDismissRequest = onDismissRequest
    ) {
        val state = uiState as ParticipateUiState.DialogState
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
                Text(text = "${state.garden.name} #${state.garden.code}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        isLoading = state is ParticipateUiState.DialogState.Loading,
                        onClick = { onConfirm(state.garden) }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutTutDatePickerDialog(
    plantingDate: String,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        return formatter.format(Date(millis))
    }

    fun getDatePickerYearRange(): IntRange {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val nextYear = currentYear + 1
        return currentYear..nextYear
    }

    fun getDateLong(date: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateLong = dateFormat.parse(date) as Date
        return dateLong.time
    }

    val datePickerState = rememberDatePickerState(
        yearRange = getDatePickerYearRange(),
        initialSelectedDateMillis = getDateLong(plantingDate)
    )
    val selectedDate = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) } ?: ""

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