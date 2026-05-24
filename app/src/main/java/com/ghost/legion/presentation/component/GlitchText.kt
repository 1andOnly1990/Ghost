package com.ghost.legion.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun GlitchText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    glitchColor1: Color = Color(0xFFFF0040),
    glitchColor2: Color = Color(0xFF00FF88),
    intensity: Float = 1f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glitch")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -intensity,
        targetValue = intensity,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glitch_x"
    )

    // Layer 1: Red offset (behind)
    Text(
        text = text,
        style = style,
        color = glitchColor1.copy(alpha = 0.4f),
        modifier = modifier.drawBehind {
            // slight chromatic offset
        }
    )

    // Layer 2: Main text (on top — Compose stacks composables)
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}
