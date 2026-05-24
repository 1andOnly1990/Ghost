package com.ghost.legion.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorldSimulationPayload(
    val elapsedHours: Float,
    val currentFactions: List<Faction>,
    val activeNodes: List<String>,
    val causalLog: List<CausalEvent>,
    val recentActionSummary: String,
    val gameState: GameStateContext
)

@Serializable
data class GameStateContext(
    val powerLevel: Int,
    val moralityScore: Int,
    val currentLocation: String
)
