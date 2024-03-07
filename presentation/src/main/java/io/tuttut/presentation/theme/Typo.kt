package io.tuttut.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.tuttut.presentation.R

private val Pretendard = FontFamily(
    Font(R.font.pretendard_regular),
    Font(R.font.pretendard_medium),
    Font(R.font.pretendard_semibold),
    Font(R.font.pretendard_bold)
)

private val BaeminEuljiro = FontFamily(
    Font(R.font.bmeuljiro)
)

val TutTutTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = BaeminEuljiro,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        color = Primary
    ),
    headlineLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        color = White
    ),
    headlineMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        color = White
    ),
    headlineSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 12.sp,
        color = White
    ),
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        color = White
    ),
    bodyMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        color = White
    ),
    labelLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        color = White
    ),
    labelMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        color = White
    ),
    labelSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 10.sp,
        color = White
    ),
    displayMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        color = White
    ),
    displaySmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 10.sp,
        color = White
    ),
)