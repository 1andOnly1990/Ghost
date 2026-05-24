package com.ghost.legion.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ghost.legion.data.local.dao.ActiveStateDao
import com.ghost.legion.data.remote.GeminiClient
import com.ghost.legion.domain.model.CausalEvent
import com.ghost.legion.domain.model.CausalTrigger
import com.ghost.legion.domain.model.GameStateContext
import com.ghost.legion.domain.model.WorldSimulationPayload
import com.ghost.legion.domain.repository.NarrativeRepository
import com.ghost.legion.domain.repository.WorldRepository
import com.ghost.legion.presentation.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class FactionTickWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val worldRepository: WorldRepository,
    private val activeStateDao: ActiveStateDao,
    private val narrativeRepository: NarrativeRepository,
    private val geminiClient: GeminiClient
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!geminiClient.isInitialized()) return Result.failure()

        try {
            // 1. Gather context
            val worldBoard = worldRepository.getWorldBoardOnce()
            val gameStateEntity = activeStateDao.getStateOnce() ?: return Result.failure()
            val recentLog = narrativeRepository.getRecentChatHistory(limit = 20).firstOrNull() ?: emptyList()

            val lastTick = worldBoard.lastTickTimestamp
            val now = System.currentTimeMillis()
            val elapsedHours = (now - lastTick).toFloat() / (1000 * 60 * 60)

            // If it's been less than an hour, don't tick (debounce)
            if (elapsedHours < 1.0f) return Result.success()

            // 2. Build Action Summary
            val actionSummary = recentLog
                .filter { !it.isPlayerMessage }
                .takeLast(5)
                .joinToString("\n") { "- ${it.summary ?: it.messageText.take(150)}" }

            val gameStateContext = GameStateContext(
                powerLevel = gameStateEntity.powerLevel,
                moralityScore = gameStateEntity.moralityScore,
                currentLocation = gameStateEntity.currentLocation
            )

            val payload = WorldSimulationPayload(
                elapsedHours = elapsedHours,
                currentFactions = worldBoard.factions,
                activeNodes = worldBoard.activeNodes,
                causalLog = worldBoard.causalLog,
                recentActionSummary = actionSummary,
                gameState = gameStateContext
            )

            // 3. Call Simulation Engine
            val response = geminiClient.runWorldSimulation(payload)

            // 4. Apply Updates
            worldRepository.applyTickResponse(response)

            // 5. Log causality
            val timeEvent = CausalEvent(
                timestamp = now,
                trigger = CausalTrigger.TIME_PASSAGE,
                description = "World advanced by ${String.format("%.1f", elapsedHours)} hours"
            )
            val factionEvents = response.factionUpdates.map {
                CausalEvent(
                    timestamp = now,
                    trigger = CausalTrigger.FACTION_RESPONSE,
                    description = it.agendaProgress,
                    factionAffected = it.factionId
                )
            }
            worldRepository.appendCausalEvents(listOf(timeEvent) + factionEvents)

            // 6. Push OS Notification
            val notifier = NotificationHelper(context)
            notifier.showMorningBrief(
                title = response.briefTitle,
                content = response.morningBrief
            )

            return Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "faction_tick_work"
    }
}
