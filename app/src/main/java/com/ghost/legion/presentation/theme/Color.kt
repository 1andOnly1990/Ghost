package com.ghost.legion.presentation.theme

import androidx.compose.ui.graphics.Color

// === NERV-STYLE: Brutalist Technical ===
object NervColors {
    val background = Color(0xFF000000)
    val surface = Color(0xFF0A0A0A)
    val surfaceVariant = Color(0xFF141414)
    val orange = Color(0xFFFF9830) // Primary NERV Orange
    val green = Color(0xFF50FF50)  // Data Green
    val red = Color(0xFFFF0000)    // Emergency Red
    val white = Color(0xFFFFFFFF)
}

// === DEVON: Operator ===
object DevonColors {
    val background = Color(0xFF000000)
    val surface = Color(0xFF0A0A0A)
    val surfaceVariant = Color(0xFF141414)
    val primary = NervColors.orange
    val primaryDim = Color(0xFF8A4B00)
    val onBackground = Color(0xFFFFB870)
    val onSurface = NervColors.orange
    val accent = NervColors.green
    val error = NervColors.red
    val textDim = Color(0xFF5A3A10)
}

// === AURA: MAGI System ===
object AuraColors {
    val background = Color(0xFF000000)
    val surface = Color(0xFF001111)
    val surfaceVariant = Color(0xFF002222)
    val primary = Color(0xFF00FFFF)        // MAGI Cyan
    val primaryDim = Color(0xFF008888)
    val onBackground = Color(0xFF88FFFF)
    val onSurface = Color(0xFF00FFFF)
    val accent = Color(0xFF00FFFF)
    val error = NervColors.red
    val textDim = Color(0xFF005555)
    val grid = Color(0x3300FFFF)           // Subtle grid overlay
    val wireframe = Color(0x6600FFFF)      // Wireframe borders
}

// === ECHO: Synthetic Core ===
object EchoColors {
    val background = Color(0xFF000000)
    val surface = Color(0xFF110022)
    val surfaceVariant = Color(0xFF220044)
    val primary = Color(0xFF8A2BE2)        // Electric Violet
    val primaryDim = Color(0xFF4B0082)
    val onBackground = Color(0xFFDDA0DD)
    val onSurface = Color(0xFF8A2BE2)
    val accent = Color(0xFFFF00FF)         // Magenta accent
    val accentAlt = Color(0xFF9B59D0)      // Alt violet
    val error = NervColors.red
    val textDim = Color(0xFF3A1A60)
    val glow = Color(0x668A2BE2)           // Soft glow
}

// === LEGION: Override ===
object LegionColors {
    val background = Color(0xFF000000)
    val surface = Color(0xFF110000)
    val surfaceVariant = Color(0xFF220000)
    val primary = Color(0xFFFFFFFF)        // Pure white core
    val amber = DevonColors.primary
    val blue = AuraColors.primary
    val violet = EchoColors.primary
    val onBackground = Color(0xFFFFFFFF)
    val onSurface = Color(0xFFCCCCCC)
    val error = NervColors.red
    val glitch1 = NervColors.red           // Chromatic aberration red
    val glitch2 = NervColors.green        // Chromatic aberration green
    val glitch3 = Color(0xFF0000FF)        // Chromatic aberration blue
}

// === SYSTEM: Neutral ===
object SystemColors {
    val background = Color(0xFF0F0F0F)
    val surface = Color(0xFF1A1A1A)
    val primary = Color(0xFF888888)
    val onBackground = Color(0xFF999999)
    val onSurface = Color(0xFF777777)
}

    // AURA — MAGI terminal
    val AuraBackground = Color(0xFF000000)
    val AuraBorder = Color(0xFF00FFFF)
    val AuraPrimary = Color(0xFF00FFFF)
    val AuraDataText = Color(0xFF88FFFF)

    // ECHO — Synthetic core
    val EchoBackground = Color(0xFF000000)
    val EchoPrimary = Color(0xFF8A2BE2)
    val EchoAccent = Color(0xFFFF00FF)
    val EchoText = Color(0xFFDDA0DD)

    // DEVON — Operator Console
    val DevonBackground = Color(0xFF000000)
    val DevonKinesicHighlight = NervColors.green
    val DevonMarginalia = NervColors.orange

    // LEGION — System Override
    val LegionBorderNanite = NervColors.red
    val LegionCoreBlue = Color(0xFF0000FF)
    val LegionPulseViolet = NervColors.red
}
