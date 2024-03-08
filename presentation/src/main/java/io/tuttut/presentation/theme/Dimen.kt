package io.tuttut.presentation.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val screenHorizontalPadding = 20.dp
private val screenVerticalPadding = 30.dp
val buttonHeight = 60.dp

fun Modifier.withScreenPadding(): Modifier {
    return this then Modifier.padding(
        start = screenHorizontalPadding,
        end = screenHorizontalPadding,
        bottom = screenVerticalPadding,
        top = 0.dp
    )
}