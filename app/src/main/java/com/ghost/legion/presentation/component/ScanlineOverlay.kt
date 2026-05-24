package com.ghost.legion.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun ScanlineOverlay(
    modifier: Modifier = Modifier,
    color: Color = Color.White.copy(alpha = 0.03f),
    lineSpacing: Float = 4f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanline")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = lineSpacing * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline_offset"
    )

    val flicker by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flicker"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val height = size.height
        val width = size.width
        var y = offset
        while (y < height) {
            // Main Scanline
            drawLine(
                color = color.copy(alpha = color.alpha * flicker),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 2f
            )
            // Chromatic aberration (Subtle red shift)
            drawLine(
                color = Color.Red.copy(alpha = 0.05f * flicker),
                start = Offset(0f, y - 1f),
                end = Offset(width, y - 1f),
                strokeWidth = 1f
            )
            // Chromatic aberration (Subtle blue shift)
            drawLine(
                color = Color.Blue.copy(alpha = 0.05f * flicker),
                start = Offset(0f, y + 1f),
                end = Offset(width, y + 1f),
                strokeWidth = 1f
            )
            y += lineSpacing
        }
    }
}
