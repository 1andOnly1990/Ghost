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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.NarrativeChoice
import com.ghost.legion.domain.model.VisualState
import com.ghost.legion.domain.model.TextSpeed
import com.ghost.legion.presentation.theme.EntityColors
import com.ghost.legion.presentation.theme.EntityFonts

@Composable
fun ChatBubble(
    message: ChatMessage,
    visualState: VisualState,
    speed: TextSpeed = TextSpeed.NORMAL,
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
                    .background(Color.Black)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RectangleShape)
                    .padding(12.dp)
            ) {
                TypewriterText(
                    fullText = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    speed = speed
                )
            }
        }
        return
    }

    when (visualState) {
        VisualState.AURA_OVERRIDE -> AuraBubble(message, speed, modifier)
        VisualState.ECHO_OVERRIDE -> EchoBubble(message, speed, modifier)
        VisualState.DEVON_KINESIC -> DevonKinesicBubble(message, speed, modifier)
        VisualState.DEVON_TUNNEL -> DevonTunnelBubble(message, speed, modifier)
        VisualState.DEVON_MARGINALIA -> DevonMarginaliaBubble(message, speed, modifier)
        VisualState.LEGION_GESTALT -> LegionBubble(message, speed, modifier)
        VisualState.BASELINE -> BaselineBubble(message, speed, modifier)
    }
}

@Composable
fun AuraBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = EntityColors.AuraBorder,
                shape = RectangleShape
            )
            .background(Color.Black)
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
            TypewriterText(
                fullText = message.text,
                color = EntityColors.AuraDataText,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 20.sp
                ),
                speed = speed
            )
        }
    }
}

@Composable
fun EchoBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
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
            .background(Color.Black)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        EntityColors.EchoPrimary.copy(alpha = alpha),
                        EntityColors.EchoAccent.copy(alpha = 0.5f)
                    )
                ),
                shape = RectangleShape
            )
            .padding(16.dp)
    ) {
        TypewriterText(
            fullText = message.text,
            color = EntityColors.EchoText,
            style = TextStyle(
                fontSize = 14.sp,
                letterSpacing = 1.5.sp,
                lineHeight = 22.sp,
                fontStyle = FontStyle.Italic
            ),
            speed = speed
        )
    }
}

@Composable
fun DevonMarginaliaBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF080808))
                .padding(12.dp)
        ) {
            TypewriterText(
                fullText = message.text,
                color = Color(0xFF607D8B),
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 20.sp
                ),
                speed = speed
            )
        }
    }
}

@Composable
fun DevonKinesicBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .border(2.dp, EntityColors.DevonKinesicHighlight.copy(alpha = 0.8f), RectangleShape)
            .padding(12.dp)
    ) {
        TypewriterText(
            fullText = message.text,
            color = EntityColors.DevonKinesicHighlight,
            style = TextStyle(
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            ),
            speed = speed
        )
    }
}

@Composable
fun DevonTunnelBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .border(2.dp, Color(0xFFFFFFFF), RectangleShape)
            .padding(16.dp)
    ) {
        TypewriterText(
            fullText = message.text,
            color = Color.White,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 22.sp
            ),
            speed = speed
        )
    }
}

@Composable
fun LegionBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
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
                width = 3.dp,
                brush = Brush.sweepGradient(
                    colors = listOf(
                        EntityColors.LegionBorderNanite,
                        EntityColors.LegionCoreBlue,
                        EntityColors.LegionPulseViolet,
                        EntityColors.LegionBorderNanite
                    )
                ),
                shape = RectangleShape
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
            TypewriterText(
                fullText = message.text,
                color = Color.White,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp
                ),
                speed = speed
            )
        }
    }
}

@Composable
fun BaselineBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), RectangleShape)
            .padding(12.dp)
    ) {
        TypewriterText(
            fullText = message.text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            speed = speed
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
                .background(Color.Black)
                .clickable { onSelected(choice.id) }
                .border(2.dp, MaterialTheme.colorScheme.primary, RectangleShape)
                .padding(16.dp)
        ) {
            Text(
                text = "> ${choice.text}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
