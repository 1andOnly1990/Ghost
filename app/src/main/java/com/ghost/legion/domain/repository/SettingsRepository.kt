package com.ghost.legion.domain.repository

import com.ghost.legion.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
}
