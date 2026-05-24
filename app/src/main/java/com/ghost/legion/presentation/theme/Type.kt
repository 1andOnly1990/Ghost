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
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = 0.3.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp
    )
)

val AuraTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = 0.3.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp
    )
)

val EchoTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = 0.3.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RetroFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp
    )
)

val LegionTypography = DevonTypography

object EntityFonts {
    val devonHandwriting = FontFamily(
        Font(R.font.caveat_regular, FontWeight.Normal)
    )
}
