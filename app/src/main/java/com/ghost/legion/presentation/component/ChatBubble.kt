package com.ghost.legion.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ghost.legion.domain.model.NarrativeEntity
import com.ghost.legion.presentation.theme.AuraColors
import com.ghost.legion.presentation.theme.DevonColors
import com.ghost.legion.presentation.theme.EchoColors
import com.ghost.legion.presentation.theme.LegionColors
import com.ghost.legion.presentation.theme.SystemColors

@Composable
fun ChatBubble(
    text: String,
    entity: NarrativeEntity,
    isPlayerMessage: Boolean,
    isLatest: Boolean = false,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(!isLatest) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val alignment = if (isPlayerMessage) Alignment.End else Alignment.Start
    val entityColor = when (entity) {
        NarrativeEntity.DEVON -> DevonColors.primary
        NarrativeEntity.AURA -> AuraColors.primary
        NarrativeEntity.ECHO -> EchoColors.primary
        NarrativeEntity.LEGION -> LegionColors.primary
        NarrativeEntity.SYSTEM -> SystemColors.primary
    }
    val bubbleBg = if (isPlayerMessage) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(
            initialOffsetX = { if (isPlayerMessage) it / 4 else -it / 4 }
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalAlignment = alignment
        ) {
            // Entity label
            if (!isPlayerMessage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(entityColor)
                    )
                    Text(
                        text = entity.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = entityColor
                    )
                }
            }

            // Message bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(
                        topStart = if (isPlayerMessage) 12.dp else 2.dp,
                        topEnd = if (isPlayerMessage) 2.dp else 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    ))
                    .background(bubbleBg)
                    .border(
                        width = 0.5.dp,
                        color = entityColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(
                            topStart = if (isPlayerMessage) 12.dp else 2.dp,
                            topEnd = if (isPlayerMessage) 2.dp else 12.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                if (isLatest && !isPlayerMessage) {
                    TypewriterText(
                        fullText = text,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
