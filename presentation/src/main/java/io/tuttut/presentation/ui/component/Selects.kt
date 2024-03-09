package io.tuttut.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TutTutSelect(
    modifier: Modifier = Modifier,
    title: String,
    isSelect: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = if (isSelect) MaterialTheme.colorScheme.surface else Color.Transparent,
                shape = MaterialTheme.shapes.large
            )
            .border(
                width = if (isSelect) 0.dp else 1.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            )
            .clickable { onSelect() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelect) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary
        )
    }
}