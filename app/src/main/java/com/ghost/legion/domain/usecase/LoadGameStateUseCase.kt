package com.ghost.legion.domain.usecase

import com.ghost.legion.domain.model.GameState
import com.ghost.legion.domain.repository.GameStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadGameStateUseCase @Inject constructor(
    private val gameStateRepository: GameStateRepository
) {
    operator fun invoke(): Flow<GameState> {
        return gameStateRepository.getGameState().map { it ?: GameState() }
    }
}
