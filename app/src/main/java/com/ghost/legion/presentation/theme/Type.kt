package com.ghost.legion.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val TerminalFont = FontFamily.Monospace
val CleanFont = FontFamily.SansSerif

val DevonTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = TerminalFont, fontWeight = FontWeight.Normal,
        fontSize = 15.sp, lineHeight = 22.sp, letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = TerminalFont, fontWeight = FontWeight.Normal,
        fontSize = 13.sp, lineHeight = 19.sp, letterSpacing = 0.3.sp
    ),
    titleLarge = TextStyle(
        fontFamily = TerminalFont, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = TerminalFont, fontWeight = FontWeight.Bold,
        fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = TerminalFont, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp
    )
)

val AuraTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Light,
        fontSize = 15.sp, lineHeight = 23.sp, letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Light,
        fontSize = 13.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Medium,
        fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Medium,
        fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp
    )
)

val EchoTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 26.sp, letterSpacing = 0.6.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 22.sp, letterSpacing = 0.4.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp, lineHeight = 32.sp, letterSpacing = 0.8.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp, lineHeight = 26.sp, letterSpacing = 0.6.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Light,
        fontSize = 12.sp, lineHeight = 18.sp, letterSpacing = 0.5.sp
    )
)

val LegionTypography = DevonTypography
