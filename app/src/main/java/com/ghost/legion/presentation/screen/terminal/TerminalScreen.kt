package com.ghost.legion.presentation.screen.terminal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghost.legion.domain.model.NarrativeEntity
import com.ghost.legion.presentation.component.*
import com.ghost.legion.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    viewModel: TerminalViewModel,
    onNavigateToWorldBoard: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()
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

    LegionTheme(
        activeEntity = if (settings.themeOverride == com.ghost.legion.domain.model.ThemeOverride.DYNAMIC) 
            uiState.activeEntity 
        else 
            NarrativeEntity.DEVON
    ) {
        val bgColor by animateColorAsState(
            targetValue = Color.Black,
            animationSpec = tween(800),
            label = "bg_color"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
        ) {
            // Overlays
            if (settings.crtEffectsEnabled) {
                TechGridOverlay(
                    gridColor = NervColors.orange.copy(alpha = 0.05f),
                    crosshairColor = NervColors.orange.copy(alpha = 0.15f)
                )
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // Top header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(CutCornerShape(bottomEnd = 16.dp))
                        .border(1.dp, NervColors.orange, CutCornerShape(bottomEnd = 16.dp))
                        .background(Color(0xFF080808))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MAGI SYSTEM // NERV",
                                color = NervColors.orange,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "LOC: ${uiState.gameState.currentLocation.replace("_", " ").uppercase()}",
                                color = NervColors.orange.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Row {
                            IconButton(onClick = onNavigateToWorldBoard) {
                                Icon(
                                    Icons.Default.Map,
                                    contentDescription = "World Board",
                                    tint = NervColors.orange
                                )
                            }
                            IconButton(onClick = onNavigateToSettings) {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = NervColors.orange.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                // If vote is pending, show warning banner
                if (uiState.currentUiData?.triadVote != null) {
                    WarningBanner(
                        text = "DECISION PROTOCOL ACTIVE",
                        primaryColor = NervColors.orange,
                        secondaryColor = Color.Black
                    )
                }

                // Chat messages
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }

                    // Welcome message if empty
                    if (uiState.messages.isEmpty() && uiState.isInitialized) {
                        item {
                            val welcomeMsg = com.ghost.legion.domain.model.ChatMessage(
                                sessionId = uiState.sessionId,
                                entity = NarrativeEntity.SYSTEM,
                                text = "Terminal active. Waiting for input...\n\n> Type something to begin your story.",
                                isPlayerMessage = false
                            )
                            ChatBubble(
                                message = welcomeMsg,
                                visualState = com.ghost.legion.domain.model.VisualState.BASELINE,
                                speed = settings.textSpeed
                            )
                        }
                    }

                    items(
                        items = uiState.messages,
                        key = { it.id }
                    ) { message ->
                        ChatBubble(
                            message = message,
                            visualState = message.uiData?.visualState ?: com.ghost.legion.domain.model.VisualState.BASELINE,
                            speed = settings.textSpeed
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
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = NervColors.green,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "[PROCESSING DATA...]",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NervColors.green,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                }

                // Status bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .clip(CutCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(Color(0xFF111111))
                        .border(1.dp, NervColors.green.copy(alpha = 0.3f), CutCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PWR:${uiState.gameState.powerTier}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = NervColors.orange,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "MOR:${uiState.gameState.moralityScore}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = if (uiState.gameState.moralityScore > 50) NervColors.green else NervColors.red,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AURA:${uiState.gameState.auraRelationship}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = AuraColors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    if (uiState.gameState.echoIntegrated) {
                        Text(
                            text = "ECHO:${uiState.gameState.echoRelationship}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = EchoColors.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Input bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                        .clip(CutCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                        .background(Color.Black)
                        .border(1.dp, NervColors.green, CutCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                        .padding(8.dp)
                        .imePadding()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ">",
                            color = NervColors.green,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = {
                                Text(
                                    text = "AWAITING INPUT...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NervColors.green.copy(alpha = 0.3f),
                                    fontFamily = FontFamily.SansSerif,
                                    letterSpacing = 1.sp
                                )
                            },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = NervColors.green,
                                fontWeight = FontWeight.Bold
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = NervColors.green
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
                                    NervColors.green
                                else
                                    NervColors.green.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }

            // High-level overlays
            if (settings.crtEffectsEnabled) {
                ScanlineOverlay()
                VignetteOverlay()
            }

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
                    containerColor = NervColors.red
                ) {
                    Text(
                        text = "ERROR: ${uiState.error}",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
    }
}
