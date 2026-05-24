package com.ghost.legion.presentation.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.NarrativeChoice
import com.ghost.legion.domain.model.VisualState
import com.ghost.legion.presentation.theme.EntityColors
import com.ghost.legion.presentation.theme.EntityFonts

@Composable
fun ChatBubble(
    message: ChatMessage,
    visualState: VisualState,
    modifier: Modifier = Modifier
) {
    if (message.isPlayerMessage) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 2.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        return
    }

    when (visualState) {
        VisualState.AURA_OVERRIDE -> AuraBubble(message, modifier)
        VisualState.ECHO_OVERRIDE -> EchoBubble(message, modifier)
        VisualState.DEVON_KINESIC -> DevonKinesicBubble(message, modifier)
        VisualState.DEVON_TUNNEL -> DevonTunnelBubble(message, modifier)
        VisualState.DEVON_MARGINALIA -> DevonMarginaliaBubble(message, modifier)
        VisualState.LEGION_GESTALT -> LegionBubble(message, modifier)
        VisualState.BASELINE -> BaselineBubble(message, modifier)
    }
}

@Composable
fun AuraBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = EntityColors.AuraBorder,
                shape = RoundedCornerShape(2.dp)
            )
            .background(EntityColors.AuraBackground)
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "AURA // TACTICAL",
                    color = EntityColors.AuraPrimary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "■ ACTIVE",
                    color = EntityColors.AuraPrimary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = EntityColors.AuraBorder, thickness = 0.5.dp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = message.text,
                color = EntityColors.AuraDataText,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun EchoBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "echo_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "echo_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        EntityColors.EchoAccent.copy(alpha = 0.3f * alpha),
                        EntityColors.EchoBackground
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        EntityColors.EchoPrimary.copy(alpha = alpha),
                        EntityColors.EchoAccent.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = message.text,
            color = EntityColors.EchoText,
            fontSize = 14.sp,
            letterSpacing = 1.5.sp,
            lineHeight = 22.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun DevonMarginaliaBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF080808))
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = Color(0xFF607D8B),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun DevonKinesicBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(EntityColors.DevonBackground)
            .border(0.5.dp, EntityColors.DevonKinesicHighlight.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        Text(
            text = message.text,
            color = EntityColors.DevonKinesicHighlight,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun DevonTunnelBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .border(1.dp, Color(0x33FFFFFF))
            .padding(16.dp)
    ) {
        Text(
            text = message.text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun LegionBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "legion_pulse")
    val borderProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000), RepeatMode.Restart),
        label = "legion_border"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                brush = Brush.sweepGradient(
                    colors = listOf(
                        EntityColors.LegionBorderNanite,
                        EntityColors.LegionCoreBlue,
                        EntityColors.LegionPulseViolet,
                        EntityColors.LegionBorderNanite
                    )
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .background(Color(0xFF030308))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "// LEGION // ENTANGLEMENT ACTIVE",
                color = EntityColors.LegionCoreBlue,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 3.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun BaselineBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(
                topStart = 2.dp,
                topEnd = 12.dp,
                bottomStart = 12.dp,
                bottomEnd = 12.dp
            ))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        Text(
            text = message.text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun AnnotatedChoiceCard(
    choice: NarrativeChoice,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        if (choice.auraProbability != null) {
            Text(
                text = "AURA: ${choice.auraProbability} probability",
                color = EntityColors.AuraPrimary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        if (choice.devonAnnotation != null) {
            Text(
                text = "↳ ${choice.devonAnnotation}",
                color = EntityColors.DevonMarginalia,
                fontSize = 16.sp,
                fontFamily = EntityFonts.devonHandwriting,
                modifier = Modifier
                    .padding(start = 12.dp, top = 2.dp)
                    .rotate(-1.5f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onSelected(choice.id) }
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                .padding(16.dp)
        ) {
            Text(
                text = choice.text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
