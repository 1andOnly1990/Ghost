package com.ghost.legion.domain.model

data class AppSettings(
    val apiKey: String = "",
    val crtEffectsEnabled: Boolean = true,
    val textSpeed: TextSpeed = TextSpeed.NORMAL,
    val ttsEnabled: Boolean = true,
    val themeOverride: ThemeOverride = ThemeOverride.DYNAMIC
)

enum class TextSpeed(val delayMs: Long) {
    INSTANT(0L),
    FAST(15L),
    NORMAL(35L),
    SLOW(80L)
}

enum class ThemeOverride {
    DYNAMIC,
    RETRO_GREEN,
    NEON_SYNTHWAVE,
    MONOCHROME_AMBER
}
