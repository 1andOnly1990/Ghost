package com.ghost.legion.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.legion.domain.model.AppSettings
import com.ghost.legion.domain.model.TextSpeed
import com.ghost.legion.domain.model.ThemeOverride
import com.ghost.legion.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun updateApiKey(key: String) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings.value.copy(apiKey = key))
        }
    }

    fun toggleCrtEffects(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings.value.copy(crtEffectsEnabled = enabled))
        }
    }

    fun updateTextSpeed(speed: TextSpeed) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings.value.copy(textSpeed = speed))
        }
    }

    fun toggleTts(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings.value.copy(ttsEnabled = enabled))
        }
    }

    fun updateThemeOverride(theme: ThemeOverride) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings.value.copy(themeOverride = theme))
        }
    }
}
