package com.ghost.legion.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghost.legion.domain.model.NarrativeChoice
import com.ghost.legion.domain.model.TriadVoteData
import com.ghost.legion.presentation.theme.AuraColors
import com.ghost.legion.presentation.theme.DevonColors
import com.ghost.legion.presentation.theme.EchoColors
import com.ghost.legion.presentation.theme.NervColors

private val CardShape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)

@Composable
fun ChoiceCard(
    choices: List<NarrativeChoice>,
    onChoiceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "DECISION REQUIRED",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        choices.forEach { choice ->
            AnnotatedChoiceCard(
                choice = choice,
                onSelected = onChoiceSelected
            )
        }
    }
}

@Composable
fun TriadVoteCard(
    voteData: TriadVoteData,
    onVote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "triad_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        border = BorderStroke(
            width = 2.dp,
            color = NervColors.red.copy(alpha = alpha)
        ),
        shape = CardShape
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MAGI SYSTEM PROTOCOL",
                    color = NervColors.red,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Box(
                    modifier = Modifier
                        .background(NervColors.red.copy(alpha = alpha))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "EMERGENCY",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "⚡ TRIAD CONVERGENCE",
                style = MaterialTheme.typography.titleLarge,
                color = NervColors.red,
                fontWeight = FontWeight.Black
            )
            
            Spacer(Modifier.height(8.dp))

            Text(
                text = voteData.question,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Devon's position
                VotePositionButton(
                    label = "MAGI-01 // OP_DEVON",
                    position = voteData.devonPosition,
                    color = DevonColors.primary,
                    onClick = { onVote(voteData.devonPosition) }
                )

                // Aura's position
                VotePositionButton(
                    label = "MAGI-02 // CASPER",
                    position = voteData.auraPosition,
                    color = AuraColors.primary,
                    onClick = { onVote(voteData.auraPosition) }
                )

                // Echo's position (if available)
                voteData.echoPosition?.let { echoPos ->
                    VotePositionButton(
                        label = "MAGI-03 // SYNTH",
                        position = echoPos,
                        color = EchoColors.primary,
                        onClick = { onVote(echoPos) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VotePositionButton(
    label: String,
    position: String,
    color: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
        border = BorderStroke(2.dp, color),
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = label,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "> $position",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
