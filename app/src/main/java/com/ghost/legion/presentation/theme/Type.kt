package com.ghost.legion.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ghost.legion.R

val TerminalFont = FontFamily.Monospace
val CleanFont = FontFamily.SansSerif

val RetroFont = FontFamily(
    Font(R.font.vt323_regular, FontWeight.Normal)
)

val DevonTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 18.sp, letterSpacing = 0.3.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Black,
        fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Bold,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.sp
    )
)

val AuraTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 18.sp, letterSpacing = 0.3.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Black,
        fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Bold,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.sp
    )
)

val EchoTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 18.sp, letterSpacing = 0.3.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Black,
        fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CleanFont, fontWeight = FontWeight.Bold,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.sp
    )
)

val LegionTypography = DevonTypography

object EntityFonts {
    val devonHandwriting = FontFamily(
        Font(R.font.caveat_regular, FontWeight.Normal)
    )
}
