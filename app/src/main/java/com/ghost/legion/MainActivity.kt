package com.ghost.legion

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.ghost.legion.data.remote.GeminiClient
import com.ghost.legion.presentation.navigation.LegionNavGraph
import com.ghost.legion.presentation.navigation.LegionRoute
import com.ghost.legion.presentation.theme.LegionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.ghost.legion.domain.repository.SettingsRepository

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var geminiClient: GeminiClient

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var apiKey by remember { mutableStateOf("") }
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()

            // Load saved API key
            LaunchedEffect(Unit) {
                settingsRepository.settings.collect { settings ->
                    if (settings.apiKey.isNotBlank()) {
                        geminiClient.initialize(settings.apiKey)
                    }
                }
            }
            
            // Initial navigation logic
            LaunchedEffect(Unit) {
                val settings = settingsRepository.settings.first()
                if (settings.apiKey.isBlank()) {
                    navController.navigate(LegionRoute.Settings.route) {
                        popUpTo(LegionRoute.Terminal.route) { inclusive = false }
                    }
                }
            }

            LegionTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    LegionNavGraph(
                        navController = navController
                    )
                }
            }
        }
    }
}
