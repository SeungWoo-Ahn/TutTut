package io.tuttut.presentation.ui.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TutTutDropDown(label: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(text = label) },
        onClick = onClick
    )
}