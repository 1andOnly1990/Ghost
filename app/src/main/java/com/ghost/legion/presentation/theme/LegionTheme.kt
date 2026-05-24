package com.ghost.legion.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.ghost.legion.domain.model.NarrativeEntity

val LocalActiveEntity = compositionLocalOf { NarrativeEntity.DEVON }

private val DevonColorScheme = darkColorScheme(
    background = DevonColors.background, surface = DevonColors.surface,
    surfaceVariant = DevonColors.surfaceVariant, primary = DevonColors.primary,
    onBackground = DevonColors.onBackground, onSurface = DevonColors.onSurface,
    error = DevonColors.error
)

private val AuraColorScheme = darkColorScheme(
    background = AuraColors.background, surface = AuraColors.surface,
    surfaceVariant = AuraColors.surfaceVariant, primary = AuraColors.primary,
    onBackground = AuraColors.onBackground, onSurface = AuraColors.onSurface,
    error = AuraColors.error
)

private val EchoColorScheme = darkColorScheme(
    background = EchoColors.background, surface = EchoColors.surface,
    surfaceVariant = EchoColors.surfaceVariant, primary = EchoColors.primary,
    onBackground = EchoColors.onBackground, onSurface = EchoColors.onSurface,
    error = EchoColors.error
)

private val LegionColorScheme = darkColorScheme(
    background = LegionColors.background, surface = LegionColors.surface,
    surfaceVariant = LegionColors.surfaceVariant, primary = LegionColors.primary,
    onBackground = LegionColors.onBackground, onSurface = LegionColors.onSurface,
    error = LegionColors.error
)

private val SystemColorScheme = darkColorScheme(
    background = SystemColors.background, surface = SystemColors.surface,
    primary = SystemColors.primary, onBackground = SystemColors.onBackground,
    onSurface = SystemColors.onSurface
)

@Composable
fun LegionTheme(
    activeEntity: NarrativeEntity = NarrativeEntity.DEVON,
    content: @Composable () -> Unit
) {
    val colorScheme = when (activeEntity) {
        NarrativeEntity.DEVON -> DevonColorScheme
        NarrativeEntity.AURA -> AuraColorScheme
        NarrativeEntity.ECHO -> EchoColorScheme
        NarrativeEntity.LEGION -> LegionColorScheme
        NarrativeEntity.SYSTEM -> SystemColorScheme
    }

    val typography = when (activeEntity) {
        NarrativeEntity.DEVON -> DevonTypography
        NarrativeEntity.AURA -> AuraTypography
        NarrativeEntity.ECHO -> EchoTypography
        NarrativeEntity.LEGION -> LegionTypography
        NarrativeEntity.SYSTEM -> AuraTypography
    }

    CompositionLocalProvider(LocalActiveEntity provides activeEntity) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
