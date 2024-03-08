package io.tuttut.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val Primary = Color(0xFF03C988)
val PrimaryDisabled = Color(0x9903C988)
val Secondary = Color(0xFF272829)
val Grey300 = Color(0xFF9D9D9D)
val Grey200 = Color(0xFFD1D9D9)
val Grey100 = Color(0xFFEFEFEF)
val Red = Color(0xFFD00036)

val DarkColors = darkColorScheme(
    primary = Primary,
    inversePrimary = PrimaryDisabled,
    background = Secondary,
    surface = Grey100,
    onSurface = Grey200,
    surfaceVariant = Grey300,
    secondary = Black,
    onSecondary = White,
    onError = Red
)