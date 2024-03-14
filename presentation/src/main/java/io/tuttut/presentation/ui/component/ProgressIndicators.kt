package io.tuttut.presentation.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TutTutLoading(
    size: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSecondary
) {
    CircularProgressIndicator(
        modifier = modifier.size(size.dp),
        color = color,
        trackColor = Color.Transparent
    )
}