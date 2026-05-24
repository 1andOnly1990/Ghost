package com.ghost.legion.domain.usecase

import com.ghost.legion.domain.model.NarrativeResponse
import com.ghost.legion.domain.repository.GameStateRepository
import com.ghost.legion.domain.repository.NarrativeRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val narrativeRepository: NarrativeRepository,
    private val gameStateRepository: GameStateRepository
) {
    suspend operator fun invoke(sessionId: String, message: String): NarrativeResponse {
        val response = narrativeRepository.sendMessage(sessionId, message)
        response.uiData?.stateChanges?.let { changes ->
            gameStateRepository.applyStateChanges(
                moralityDelta = changes.moralityDelta,
                auraRelationshipDelta = changes.auraRelationshipDelta,
                echoRelationshipDelta = changes.echoRelationshipDelta,
                newItems = changes.newItems,
                removedItems = changes.removedItems,
                locationChange = changes.locationChange,
                powerLevelChange = changes.powerLevelChange
            )
        }
        return response
    }
}
