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
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    fullText: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    charDelayMs: Long = 25L,
    onComplete: () -> Unit = {}
) {
    var charCount by remember(fullText) { mutableIntStateOf(0) }

    LaunchedEffect(fullText) {
        charCount = 0
        for (i in fullText.indices) {
            delay(charDelayMs)
            charCount = i + 1
        }
        onComplete()
    }

    Text(
        text = fullText.take(charCount) + if (charCount < fullText.length) "▌" else "",
        style = style,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}
