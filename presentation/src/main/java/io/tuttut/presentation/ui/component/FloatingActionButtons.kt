package io.tuttut.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TutTutFAB(modifier: Modifier = Modifier, text: String, expanded: Boolean, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        expanded = expanded,
        text = { Text(text = text, style = MaterialTheme.typography.bodySmall) },
        icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = "fab-icon") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        onClick = onClick
    )
}