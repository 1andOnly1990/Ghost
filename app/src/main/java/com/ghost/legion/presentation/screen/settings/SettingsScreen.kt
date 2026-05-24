package com.ghost.legion.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ghost.legion.domain.model.NarrativeEntity
import com.ghost.legion.domain.model.TextSpeed
import com.ghost.legion.domain.model.ThemeOverride
import com.ghost.legion.presentation.theme.DevonColors
import com.ghost.legion.presentation.theme.LegionTheme
import com.ghost.legion.presentation.theme.LegionColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    var tempApiKey by remember(settings.apiKey) { mutableStateOf(settings.apiKey) }
    var saved by remember { mutableStateOf(false) }

    LegionTheme(activeEntity = NarrativeEntity.DEVON) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black) // Neo Genesis deep black
        ) {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "SYSTEM_CONFIG",
                            style = MaterialTheme.typography.titleLarge,
                            color = LegionColors.glitch2
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = LegionColors.glitch2
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // --- API KEY SECTION ---
                    SectionBox(title = "GEMINI CORE UPLINK") {
                        OutlinedTextField(
                            value = tempApiKey,
                            onValueChange = {
                                tempApiKey = it
                                saved = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("API Key") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LegionColors.glitch2,
                                unfocusedBorderColor = DevonColors.primaryDim,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = LegionColors.glitch2,
                                focusedLabelColor = LegionColors.glitch2,
                                unfocusedLabelColor = DevonColors.primaryDim
                            ),
                            shape = RectangleShape
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.updateApiKey(tempApiKey)
                                saved = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = tempApiKey.isNotBlank(),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LegionColors.glitch2,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = if (saved) "> LINK ESTABLISHED" else "> INIT LINK",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    // --- VISUALS SECTION ---
                    SectionBox(title = "VISUAL OPTICS") {
                        ToggleRow(
                            label = "CRT SCANLINES & PHOSPHOR",
                            checked = settings.crtEffectsEnabled,
                            onCheckedChange = { viewModel.toggleCrtEffects(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        var themeMenuExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = themeMenuExpanded,
                            onExpandedChange = { themeMenuExpanded = !themeMenuExpanded }
                        ) {
                            OutlinedTextField(
                                value = settings.themeOverride.name.replace("_", " "),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("COLOR OVERRIDE") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = themeMenuExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LegionColors.glitch2,
                                    unfocusedBorderColor = DevonColors.primaryDim,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RectangleShape
                            )
                            ExposedDropdownMenu(
                                expanded = themeMenuExpanded,
                                onDismissRequest = { themeMenuExpanded = false }
                            ) {
                                ThemeOverride.values().forEach { theme ->
                                    DropdownMenuItem(
                                        text = { Text(theme.name.replace("_", " ")) },
                                        onClick = {
                                            viewModel.updateThemeOverride(theme)
                                            themeMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // --- AUDIO & TEXT SECTION ---
                    SectionBox(title = "FEEDBACK SYSTEMS") {
                        ToggleRow(
                            label = "A.U.R.A. TTS ENGINE",
                            checked = settings.ttsEnabled,
                            onCheckedChange = { viewModel.toggleTts(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        var speedMenuExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = speedMenuExpanded,
                            onExpandedChange = { speedMenuExpanded = !speedMenuExpanded }
                        ) {
                            OutlinedTextField(
                                value = settings.textSpeed.name,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("TEXT RENDERING SPEED") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = speedMenuExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LegionColors.glitch2,
                                    unfocusedBorderColor = DevonColors.primaryDim,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RectangleShape
                            )
                            ExposedDropdownMenu(
                                expanded = speedMenuExpanded,
                                onDismissRequest = { speedMenuExpanded = false }
                            ) {
                                TextSpeed.values().forEach { speed ->
                                    DropdownMenuItem(
                                        text = { Text(speed.name) },
                                        onClick = {
                                            viewModel.updateTextSpeed(speed)
                                            speedMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // --- ABOUT ---
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "LEGION v0.2.0 // RETRO_GENESIS_BUILD\nPOWERED BY GEMINI 2.0 FLASH",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionBox(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, DevonColors.primaryDim, RectangleShape)
            .padding(16.dp)
    ) {
        Text(
            text = ":: $title ::",
            style = MaterialTheme.typography.titleMedium,
            color = DevonColors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = LegionColors.glitch2,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}
