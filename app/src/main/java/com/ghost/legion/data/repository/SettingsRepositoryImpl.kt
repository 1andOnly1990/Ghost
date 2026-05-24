package com.ghost.legion.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ghost.legion.domain.model.AppSettings
import com.ghost.legion.domain.model.TextSpeed
import com.ghost.legion.domain.model.ThemeOverride
import com.ghost.legion.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "legion_settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : SettingsRepository {

    private object PreferencesKeys {
        val API_KEY = stringPreferencesKey("gemini_api_key")
        val CRT_EFFECTS = booleanPreferencesKey("crt_effects")
        val TEXT_SPEED = stringPreferencesKey("text_speed")
        val TTS_ENABLED = booleanPreferencesKey("tts_enabled")
        val THEME_OVERRIDE = stringPreferencesKey("theme_override")
    }

    override val settings: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        AppSettings(
            apiKey = preferences[PreferencesKeys.API_KEY] ?: "",
            crtEffectsEnabled = preferences[PreferencesKeys.CRT_EFFECTS] ?: true,
            textSpeed = try { TextSpeed.valueOf(preferences[PreferencesKeys.TEXT_SPEED] ?: "NORMAL") } catch (e: Exception) { TextSpeed.NORMAL },
            ttsEnabled = preferences[PreferencesKeys.TTS_ENABLED] ?: true,
            themeOverride = try { ThemeOverride.valueOf(preferences[PreferencesKeys.THEME_OVERRIDE] ?: "DYNAMIC") } catch (e: Exception) { ThemeOverride.DYNAMIC }
        )
    }

    override suspend fun updateSettings(settings: AppSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.API_KEY] = settings.apiKey
            preferences[PreferencesKeys.CRT_EFFECTS] = settings.crtEffectsEnabled
            preferences[PreferencesKeys.TEXT_SPEED] = settings.textSpeed.name
            preferences[PreferencesKeys.TTS_ENABLED] = settings.ttsEnabled
            preferences[PreferencesKeys.THEME_OVERRIDE] = settings.themeOverride.name
        }
    }
}
