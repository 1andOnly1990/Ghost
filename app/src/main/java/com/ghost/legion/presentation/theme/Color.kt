package com.ghost.legion.presentation.theme

import androidx.compose.ui.graphics.Color

// === DEVON: Noir Terminal ===
object DevonColors {
    val background = Color(0xFF0A0A0A)
    val surface = Color(0xFF141414)
    val surfaceVariant = Color(0xFF1E1E1E)
    val primary = Color(0xFFD4A855)        // Warm amber
    val primaryDim = Color(0xFF8B7038)
    val onBackground = Color(0xFFC2A668)   // Amber text
    val onSurface = Color(0xFF9B8A5E)
    val accent = Color(0xFFE8C56D)
    val error = Color(0xFFCF6679)
    val textDim = Color(0xFF5C5340)
}

// === AURA: Clinical Ice ===
object AuraColors {
    val background = Color(0xFF050A10)
    val surface = Color(0xFF0A1628)
    val surfaceVariant = Color(0xFF0F1F38)
    val primary = Color(0xFF5BA3D9)        // Clinical blue
    val primaryDim = Color(0xFF2E6A9E)
    val onBackground = Color(0xFFB8D4EA)
    val onSurface = Color(0xFF8ABED8)
    val accent = Color(0xFF7EC8E3)
    val error = Color(0xFFE57373)
    val textDim = Color(0xFF3A5F80)
    val grid = Color(0x155BA3D9)           // Subtle grid overlay
    val wireframe = Color(0x335BA3D9)      // Wireframe borders
}

// === ECHO: Synesthetic Violet ===
object EchoColors {
    val background = Color(0xFF0A050F)
    val surface = Color(0xFF150A20)
    val surfaceVariant = Color(0xFF1F1030)
    val primary = Color(0xFFB57EDC)        // Fluid violet
    val primaryDim = Color(0xFF7B4FA0)
    val onBackground = Color(0xFFD4B8E8)
    val onSurface = Color(0xFFC09ED8)
    val accent = Color(0xFFE87DBA)         // Magenta accent
    val accentAlt = Color(0xFF9B59D0)      // Alt violet
    val error = Color(0xFFFF6B9D)
    val textDim = Color(0xFF5A3A70)
    val glow = Color(0x33B57EDC)           // Soft glow
}

// === LEGION: Glitch Storm ===
object LegionColors {
    val background = Color(0xFF050505)
    val surface = Color(0xFF0C0C0C)
    val surfaceVariant = Color(0xFF141414)
    val primary = Color(0xFFFFFFFF)        // Pure white core
    val amber = DevonColors.primary
    val blue = AuraColors.primary
    val violet = EchoColors.primary
    val onBackground = Color(0xFFE0E0E0)
    val onSurface = Color(0xFFB0B0B0)
    val error = Color(0xFFFF4444)
    val glitch1 = Color(0xFFFF0040)        // Chromatic aberration red
    val glitch2 = Color(0xFF00FF88)        // Chromatic aberration green
    val glitch3 = Color(0xFF4040FF)        // Chromatic aberration blue
}

// === SYSTEM: Neutral ===
object SystemColors {
    val background = Color(0xFF0F0F0F)
    val surface = Color(0xFF1A1A1A)
    val primary = Color(0xFF888888)
    val onBackground = Color(0xFF999999)
    val onSurface = Color(0xFF777777)
}
