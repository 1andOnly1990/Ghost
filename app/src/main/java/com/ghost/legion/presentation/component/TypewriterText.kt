package com.ghost.legion.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import com.ghost.legion.domain.model.TextSpeed
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    fullText: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onBackground,
    speed: TextSpeed = TextSpeed.NORMAL,
    onComplete: () -> Unit = {}
) {
    if (speed == TextSpeed.INSTANT) {
        Text(
            text = fullText,
            style = style,
            color = color,
            modifier = modifier
        )
        LaunchedEffect(Unit) { onComplete() }
        return
    }

    var charCount by remember(fullText, speed) { mutableIntStateOf(0) }

    LaunchedEffect(fullText, speed) {
        charCount = 0
        for (i in fullText.indices) {
            delay(speed.delayMs)
            charCount = i + 1
        }
        onComplete()
    }

    Text(
        text = fullText.take(charCount) + if (charCount < fullText.length) "▌" else "",
        style = style,
        color = color,
        modifier = modifier
    )
}
