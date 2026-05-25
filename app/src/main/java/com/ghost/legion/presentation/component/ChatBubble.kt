package com.ghost.legion.presentation.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
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

private val NervShape = CutCornerShape(
    topStart = 8.dp,
    topEnd = 0.dp,
    bottomEnd = 8.dp,
    bottomStart = 0.dp
)

private val NervShapeAlt = CutCornerShape(
    topStart = 0.dp,
    topEnd = 8.dp,
    bottomEnd = 0.dp,
    bottomStart = 8.dp
)

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
                    .clip(NervShapeAlt)
                    .background(Color.Black)
                    .border(1.dp, MaterialTheme.colorScheme.primary, NervShapeAlt)
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "[TX.UPLINK]",
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "OP_DEVON",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    TypewriterText(
                        fullText = message.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        speed = speed
                    )
                }
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
            .clip(NervShape)
            .border(
                width = 2.dp,
                color = EntityColors.AuraBorder,
                shape = NervShape
            )
            .background(Color.Black)
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MAGI // CASPER-01",
                    color = EntityColors.AuraPrimary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Box(
                    modifier = Modifier
                        .background(EntityColors.AuraPrimary)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ACTIVE",
                        color = Color.Black,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = EntityColors.AuraBorder, thickness = 1.dp)
            Spacer(Modifier.height(8.dp))
            TypewriterText(
                fullText = message.text,
                color = EntityColors.AuraDataText,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
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
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "echo_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(NervShape)
            .background(Color.Black)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        EntityColors.EchoPrimary.copy(alpha = alpha),
                        EntityColors.EchoAccent.copy(alpha = 0.5f)
                    )
                ),
                shape = NervShape
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "SYNTH // ECHO",
                color = EntityColors.EchoPrimary,
                fontSize = 10.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(6.dp))
            TypewriterText(
                fullText = message.text,
                color = EntityColors.EchoText,
                style = TextStyle(
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    lineHeight = 22.sp,
                    fontStyle = FontStyle.Italic
                ),
                speed = speed
            )
        }
    }
}

@Composable
fun DevonMarginaliaBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CutCornerShape(bottomEnd = 16.dp))
                .background(Color(0xFF0A0A0A))
                .border(1.dp, EntityColors.DevonMarginalia.copy(alpha = 0.3f), CutCornerShape(bottomEnd = 16.dp))
                .padding(12.dp)
        ) {
            TypewriterText(
                fullText = message.text,
                color = EntityColors.DevonMarginalia,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = EntityFonts.devonHandwriting,
                    lineHeight = 22.sp
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
            .clip(NervShape)
            .background(Color.Black)
            .border(1.dp, EntityColors.DevonKinesicHighlight.copy(alpha = 0.8f), NervShape)
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = "[KINESIC.SCAN]",
                color = EntityColors.DevonKinesicHighlight.copy(alpha = 0.7f),
                fontSize = 10.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(6.dp))
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
}

@Composable
fun DevonTunnelBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(NervShape)
            .background(Color.Black)
            .border(2.dp, Color(0xFFFFFFFF), NervShape)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "!!! TUNNEL_VISION !!!",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(6.dp))
            TypewriterText(
                fullText = message.text,
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                ),
                speed = speed
            )
        }
    }
}

@Composable
fun LegionBubble(message: ChatMessage, speed: TextSpeed, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "legion_pulse")
    val borderProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart),
        label = "legion_border"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(NervShape)
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
                shape = NervShape
            )
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SYS // OVERRIDE",
                    color = EntityColors.LegionPulseViolet,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
                Box(
                    modifier = Modifier
                        .background(EntityColors.LegionBorderNanite)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "CRITICAL",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black
                    )
                }
            }
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
            .clip(NervShape)
            .background(Color.Black)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), NervShape)
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = "[RX.DATA]",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 9.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(4.dp))
            TypewriterText(
                fullText = message.text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                speed = speed
            )
        }
    }
}

@Composable
fun AnnotatedChoiceCard(
    choice: NarrativeChoice,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 6.dp)) {
        if (choice.auraProbability != null) {
            Text(
                text = "MAGI_PROBABILITY: ${choice.auraProbability}",
                color = EntityColors.AuraPrimary,
                fontSize = 11.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            )
        }
        if (choice.devonAnnotation != null) {
            Text(
                text = "↳ ${choice.devonAnnotation}",
                color = EntityColors.DevonMarginalia,
                fontSize = 18.sp,
                fontFamily = EntityFonts.devonHandwriting,
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 4.dp)
                    .rotate(-2f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                .background(Color.Black)
                .clickable { onSelected(choice.id) }
                .border(2.dp, MaterialTheme.colorScheme.primary, CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = choice.text,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "EXECUTE",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
