package com.ghost.legion.presentation.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WarningBanner(
    text: String = "EMERGENCY",
    primaryColor: Color = Color.Red,
    secondaryColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "banner_scroll")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40.dp.value, // Scroll by the width of one stripe
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scroll_offset"
    )

    val blinkAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(primaryColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stripeWidth = 40.dp.toPx()
            val o = offset * density
            var x = -stripeWidth + o

            while (x < size.width + stripeWidth) {
                // Draw a skewed rectangle (polygon) for the diagonal stripe
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(x, 0f)
                    lineTo(x + stripeWidth * 0.5f, 0f)
                    lineTo(x - stripeWidth * 0.5f, size.height)
                    lineTo(x - stripeWidth, size.height)
                    close()
                }
                drawPath(
                    path = path,
                    color = secondaryColor.copy(alpha = 0.8f)
                )
                x += stripeWidth
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = primaryColor.copy(alpha = blinkAlpha),
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )
        }
    }
}
