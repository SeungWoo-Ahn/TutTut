package io.tuttut.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TutTutTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = TutTutTypography,
        shapes = TutTutShapes,
        content = content
    )
}