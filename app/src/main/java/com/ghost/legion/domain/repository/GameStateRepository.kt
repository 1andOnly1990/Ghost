package com.ghost.legion.domain.repository

import com.ghost.legion.domain.model.GameState
import kotlinx.coroutines.flow.Flow

interface GameStateRepository {
    fun getGameState(): Flow<GameState?>
    suspend fun getGameStateOnce(): GameState?
    suspend fun saveGameState(state: GameState)
    suspend fun applyStateChanges(
        moralityDelta: Int? = null,
        auraRelationshipDelta: Int? = null,
        echoRelationshipDelta: Int? = null,
        newItems: List<String>? = null,
        removedItems: List<String>? = null,
        locationChange: String? = null,
        powerLevelChange: Int? = null
    )
    suspend fun initializeNewGame()
}
