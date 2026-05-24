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
import androidx.compose.ui.graphics.RectangleShape
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "// CHOOSE YOUR PATH //",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.Black
        ),
        border = BorderStroke(
            width = 2.dp,
            color = DevonColors.primary
        ),
        shape = RectangleShape
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
        shape = RectangleShape,
        border = BorderStroke(2.dp, color),
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            containerColor = androidx.compose.ui.graphics.Color.Black
        )
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
                text = "> $position",
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}
