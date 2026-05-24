package com.ghost.legion.presentation.screen.terminal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ghost.legion.domain.model.NarrativeEntity
import com.ghost.legion.presentation.component.ChatBubble
import com.ghost.legion.presentation.component.ChoiceCard
import com.ghost.legion.presentation.component.ScanlineOverlay
import com.ghost.legion.presentation.component.TriadVoteCard
import com.ghost.legion.presentation.component.VignetteOverlay
import com.ghost.legion.presentation.theme.AuraColors
import com.ghost.legion.presentation.theme.DevonColors
import com.ghost.legion.presentation.theme.EchoColors
import com.ghost.legion.presentation.theme.LegionColors
import com.ghost.legion.presentation.theme.LegionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    viewModel: TerminalViewModel,
    onNavigateToWorldBoard: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom on new messages
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    // Start session when screen loads
    LaunchedEffect(Unit) {
        if (!uiState.isInitialized) {
            viewModel.startSession()
        }
    }

    LegionTheme(activeEntity = uiState.activeEntity) {
        val bgColor by animateColorAsState(
            targetValue = MaterialTheme.colorScheme.background,
            animationSpec = tween(800),
            label = "bg_color"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top bar
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (uiState.activeEntity) {
                                            NarrativeEntity.DEVON -> DevonColors.primary
                                            NarrativeEntity.AURA -> AuraColors.primary
                                            NarrativeEntity.ECHO -> EchoColors.primary
                                            NarrativeEntity.LEGION -> LegionColors.primary
                                            NarrativeEntity.SYSTEM -> Color.Gray
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "LEGION",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "// ${uiState.gameState.currentLocation.replace("_", " ").uppercase()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToWorldBoard) {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = "World Board",
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                // Chat messages
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Welcome message if empty
                    if (uiState.messages.isEmpty() && uiState.isInitialized) {
                        item {
                            ChatBubble(
                                text = "Terminal active. Waiting for input...\n\n> Type something to begin your story.",
                                entity = NarrativeEntity.SYSTEM,
                                isPlayerMessage = false,
                                isLatest = true
                            )
                        }
                    }

                    items(
                        items = uiState.messages,
                        key = { it.id }
                    ) { message ->
                        ChatBubble(
                            text = message.text,
                            entity = message.entity,
                            isPlayerMessage = message.isPlayerMessage,
                            isLatest = message == uiState.messages.lastOrNull()
                        )
                    }

                    // Choices
                    uiState.currentUiData?.choices?.let { choices ->
                        if (choices.isNotEmpty()) {
                            item {
                                ChoiceCard(
                                    choices = choices,
                                    onChoiceSelected = { viewModel.selectChoice(it) }
                                )
                            }
                        }
                    }

                    // Triad vote
                    uiState.currentUiData?.triadVote?.let { voteData ->
                        item {
                            TriadVoteCard(
                                voteData = voteData,
                                onVote = { viewModel.castTriadVote(it) }
                            )
                        }
                    }

                    // Loading indicator
                    if (uiState.isLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Processing...",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                // Status bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PWR:${uiState.gameState.powerTier}",
                        style = MaterialTheme.typography.labelSmall,
                        color = DevonColors.primary.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "MOR:${uiState.gameState.moralityScore}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (uiState.gameState.moralityScore > 50)
                            AuraColors.primary.copy(alpha = 0.6f)
                        else
                            EchoColors.primary.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "AURA:${uiState.gameState.auraRelationship}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraColors.primary.copy(alpha = 0.6f)
                    )
                    if (uiState.gameState.echoIntegrated) {
                        Text(
                            text = "ECHO:${uiState.gameState.echoRelationship}",
                            style = MaterialTheme.typography.labelSmall,
                            color = EchoColors.primary.copy(alpha = 0.6f)
                        )
                    }
                }

                // Input bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "> Enter command...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (inputText.isNotBlank() && !uiState.isLoading) {
                                    viewModel.sendMessage(inputText)
                                    inputText = ""
                                }
                            }
                        ),
                        singleLine = true,
                        enabled = !uiState.isLoading
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && !uiState.isLoading) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank() && !uiState.isLoading
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank())
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }

            // Overlays
            ScanlineOverlay()
            VignetteOverlay()

            // Error snackbar
            AnimatedVisibility(
                visible = uiState.error != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = uiState.error ?: "",
                        color = Color.White
                    )
                }
            }
        }
    }
}
