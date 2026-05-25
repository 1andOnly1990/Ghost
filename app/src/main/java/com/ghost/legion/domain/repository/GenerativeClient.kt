package com.ghost.legion.domain.repository

import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.GameState
import com.ghost.legion.domain.model.NarrativeResponse
import com.ghost.legion.domain.model.WorldSimulationPayload
import com.ghost.legion.domain.model.WorldTickResponse

interface GenerativeClient {
    fun initialize(apiKey: String = "")
    fun isInitialized(): Boolean

    suspend fun generateNarrative(
        gameState: GameState,
        factions: List<Faction>,
        chatHistory: List<ChatMessage>,
        playerInput: String
    ): NarrativeResponse

    suspend fun runWorldSimulation(payload: WorldSimulationPayload): WorldTickResponse
}
