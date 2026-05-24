package com.ghost.legion.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghost.legion.domain.model.NarrativeChoice
import com.ghost.legion.domain.model.TriadVoteData
import com.ghost.legion.presentation.theme.AuraColors
import com.ghost.legion.presentation.theme.DevonColors
import com.ghost.legion.presentation.theme.EchoColors

@Composable
fun ChoiceCard(
    choices: List<NarrativeChoice>,
    onChoiceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "CHOOSE YOUR PATH",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            choices.forEach { choice ->
                val riskColor by animateColorAsState(
                    targetValue = when (choice.riskLevel.uppercase()) {
                        "CRITICAL" -> MaterialTheme.colorScheme.error
                        "HIGH" -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        "MEDIUM" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    },
                    animationSpec = tween(300),
                    label = "risk_color"
                )

                OutlinedButton(
                    onClick = { onChoiceSelected(choice.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, riskColor.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = choice.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = choice.riskLevel,
                            style = MaterialTheme.typography.labelSmall,
                            color = riskColor,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TriadVoteCard(
    voteData: TriadVoteData,
    onVote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = DevonColors.primary.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "⚡ TRIAD CONVERGENCE",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = voteData.question,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontStyle = FontStyle.Italic
            )

            // Devon's position
            VotePositionButton(
                label = "DEVON",
                position = voteData.devonPosition,
                color = DevonColors.primary,
                onClick = { onVote(voteData.devonPosition) }
            )

            // Aura's position
            VotePositionButton(
                label = "AURA",
                position = voteData.auraPosition,
                color = AuraColors.primary,
                onClick = { onVote(voteData.auraPosition) }
            )

            // Echo's position (if available)
            voteData.echoPosition?.let { echoPos ->
                VotePositionButton(
                    label = "ECHO",
                    position = echoPos,
                    color = EchoColors.primary,
                    onClick = { onVote(echoPos) }
                )
            }
        }
    }
}

@Composable
private fun VotePositionButton(
    label: String,
    position: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = position,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
