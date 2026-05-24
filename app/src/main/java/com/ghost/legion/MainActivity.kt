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
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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

private val Context.dataStore by preferencesDataStore(name = "legion_settings")
private val API_KEY_PREF = stringPreferencesKey("gemini_api_key")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var geminiClient: GeminiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var apiKey by remember { mutableStateOf("") }
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()

            // Load saved API key
            LaunchedEffect(Unit) {
                val savedKey = dataStore.data.map { prefs ->
                    prefs[API_KEY_PREF] ?: ""
                }.first()

                apiKey = savedKey
                if (savedKey.isNotBlank()) {
                    geminiClient.initialize(savedKey)
                } else {
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
                        navController = navController,
                        apiKey = apiKey,
                        onSaveApiKey = { newKey ->
                            apiKey = newKey
                            geminiClient.initialize(newKey)
                            scope.launch {
                                dataStore.edit { prefs ->
                                    prefs[API_KEY_PREF] = newKey
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
