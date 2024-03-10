package io.tuttut.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class SupportingTextType {
    INFO, ERROR, NONE
}

@Composable
fun TutTutTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeHolder: String,
    supportingText: String = "",
    supportingTextType: SupportingTextType = SupportingTextType.INFO,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
    onResetValue: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            value = value,
            textStyle = MaterialTheme.typography.labelLarge,
            placeholder = { Text(text = placeHolder, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.surfaceVariant) },
            trailingIcon = { if (value.isNotEmpty()) XCircle(size = 20, onClick = onResetValue) },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            onValueChange = onValueChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = supportingText,
            style = MaterialTheme.typography.displayMedium,
            color = when (supportingTextType) {
                SupportingTextType.INFO -> MaterialTheme.colorScheme.onSurface
                SupportingTextType.ERROR -> MaterialTheme.colorScheme.onError
                SupportingTextType.NONE -> Color.Transparent
            }
        )
    }
}