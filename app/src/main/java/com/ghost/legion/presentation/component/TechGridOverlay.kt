package com.ghost.legion.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TechGridOverlay(
    gridColor: Color = Color(0xFF00FF00).copy(alpha = 0.1f),
    crosshairColor: Color = Color(0xFF00FF00).copy(alpha = 0.3f),
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val gridSize = 40.dp.toPx()
        val width = size.width
        val height = size.height

        // Draw vertical lines
        var x = 0f
        while (x < width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
            x += gridSize
        }

        // Draw horizontal lines
        var y = 0f
        while (y < height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += gridSize
        }

        // Draw intersection crosshairs
        x = gridSize
        while (x < width) {
            y = gridSize
            while (y < height) {
                // Horizontal part of crosshair
                drawLine(
                    color = crosshairColor,
                    start = Offset(x - 4.dp.toPx(), y),
                    end = Offset(x + 4.dp.toPx(), y),
                    strokeWidth = 2f
                )
                // Vertical part of crosshair
                drawLine(
                    color = crosshairColor,
                    start = Offset(x, y - 4.dp.toPx()),
                    end = Offset(x, y + 4.dp.toPx()),
                    strokeWidth = 2f
                )
                y += gridSize * 4 // Only every 4th intersection
            }
            x += gridSize * 4
        }
    }
}
