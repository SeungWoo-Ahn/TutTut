package io.tuttut.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val WhiteDisabled = Color(0x0CFFFFFF)
val Primary = Color(0xFF03C988)
val PrimaryContainer = Color(0xFF8CC9B5)
val PrimaryDisabled = Color(0x0D03C988)
val Secondary = Color(0xFF272829)
val Grey800 = Color(0xFF494A4B)
val Grey700 = Color(0xFF68696A)
val Grey300 = Color(0xFF9D9D9D)
val Grey200 = Color(0xFFD1D9D9)
val Grey100 = Color(0xFFEFEFEF)
val Red = Color(0xFFD00036)
val SkyBlue = Color(0xFF50C4ED)

val DarkColors = darkColorScheme(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    inversePrimary = PrimaryDisabled,
    background = Secondary,
    surface = Grey100,
    onSurface = Grey200,
    surfaceVariant = Grey300,
    inverseSurface = Grey800,
    inverseOnSurface = Grey700,
    secondary = Black,
    onSecondary = White,
    onSecondaryContainer = WhiteDisabled,
    onError = Red,
    tertiary = SkyBlue
)