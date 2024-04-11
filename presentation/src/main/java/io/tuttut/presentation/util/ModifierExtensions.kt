package io.tuttut.presentation.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.theme.screenVerticalPadding

@Stable
fun Modifier.withScreenPadding(): Modifier {
    return this then Modifier.padding(
        start = screenHorizontalPadding,
        end = screenHorizontalPadding,
        bottom = screenVerticalPadding,
        top = 0.dp
    )
}

@Stable
fun Modifier.clickableWithOutRipple(
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource
): Modifier {
    return this then Modifier.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
}