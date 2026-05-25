package com.ghost.legion.data.local

import com.ghost.legion.domain.model.*
import com.ghost.legion.domain.repository.GenerativeClient
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockGenerativeClient @Inject constructor() : GenerativeClient {

    private var initialized = false

    override fun initialize(apiKey: String) {
        initialized = true
    }

    override fun isInitialized(): Boolean = initialized

    override suspend fun generateNarrative(
        gameState: GameState,
        factions: List<Faction>,
        chatHistory: List<ChatMessage>,
        playerInput: String
    ): NarrativeResponse {
        delay(1000) // Simulate network latency

        val isCombat = playerInput.contains("attack", ignoreCase = true) || playerInput.contains("shoot", ignoreCase = true)
        val isAura = playerInput.contains("aura", ignoreCase = true)
        val isEcho = playerInput.contains("echo", ignoreCase = true)

        val entity = when {
            isAura -> NarrativeEntity.AURA.name
            isEcho -> NarrativeEntity.ECHO.name
            else -> NarrativeEntity.DEVON.name
        }

        val textResponse = when {
            isCombat -> "We engaged the target. High probability of retaliatory action. Stay frosty."
            isAura -> "I have calculated a 94.3% chance of success if we proceed linearly. Your pulse is elevated, Devon."
            isEcho -> "Loud noises. Bright colors. The rain tastes like copper and static..."
            else -> "I observed the perimeter. Nothing but neon and rain. What's our next move?"
        }

        val visualState = when {
            isAura -> VisualState.AURA_OVERRIDE
            isEcho -> VisualState.ECHO_OVERRIDE
            isCombat -> VisualState.DEVON_TUNNEL
            else -> VisualState.BASELINE
        }

        return NarrativeResponse(
            entity = entity,
            textResponse = textResponse,
            uiData = NarrativeUiData(
                tone = "MOCK",
                visualState = visualState,
                choices = if (!isCombat) listOf(
                    NarrativeChoice("1", "Advance cautiously", "LOW", "88%", "Good plan."),
                    NarrativeChoice("2", "Go loud", "HIGH", "12%", "Stupid plan. I love it.")
                ) else null
            )
        )
    }

    override suspend fun runWorldSimulation(payload: WorldSimulationPayload): WorldTickResponse {
        delay(1500)
        return WorldTickResponse(
            factionUpdates = payload.factions.map {
                FactionUpdate(
                    factionId = it.id,
                    newStatus = "Patrolling sector",
                    alertDelta = 0,
                    agendaProgress = "Routine operations nominal.",
                    devonCaused = false
                )
            },
            nodeResolutions = emptyList(),
            morningBrief = "The city sleeps, but the factions are restless.",
            briefTitle = "NIGHT WATCH",
            newOpportunities = listOf("A mysterious signal was intercepted in Sector 4.")
        )
    }
}
