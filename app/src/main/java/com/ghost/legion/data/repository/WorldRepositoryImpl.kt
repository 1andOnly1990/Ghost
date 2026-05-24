package com.ghost.legion.data.repository

import com.ghost.legion.data.local.dao.WorldBoardDao
import com.ghost.legion.data.local.entity.WorldBoardEntity
import com.ghost.legion.domain.model.CausalEvent
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.WorldTickResponse
import com.ghost.legion.domain.repository.WorldRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldRepositoryImpl @Inject constructor(
    private val worldBoardDao: WorldBoardDao
) : WorldRepository {

    override fun getAllFactions(): Flow<List<Faction>> {
        return worldBoardDao.getWorldBoardFlow().filterNotNull().map { it.factions }
    }

    override fun getHostileFactions(): Flow<List<Faction>> {
        return worldBoardDao.getWorldBoardFlow().filterNotNull().map { board ->
            board.factions.filter { it.hostilityToPlayer >= 70 }
        }
    }

    override fun getWorldBoardFlow(): Flow<WorldBoardEntity> {
        return worldBoardDao.getWorldBoardFlow().filterNotNull()
    }

    override suspend fun getWorldBoardOnce(): WorldBoardEntity {
        return worldBoardDao.getWorldBoardOnce() ?: WorldBoardEntity()
    }

    override suspend fun initializeWorldBoard() {
        if (worldBoardDao.getWorldBoardOnce() != null) return

        val defaultFactions = listOf(
            Faction("black_sun", "Black Sun Solutions", 70, 10, "Searching for the lost courier package. Standard recovery protocols in effect.", System.currentTimeMillis(), true),
            Faction("pantheon", "The Pantheon", 95, 0, "Maintaining equilibrium in Veridia City. Business as usual.", System.currentTimeMillis(), true),
            Faction("couriers", "Independent Couriers Guild", 25, 0, "Keeping the network alive. Silas Vane's disappearance has people spooked.", System.currentTimeMillis(), true),
            Faction("onyx_ward", "Onyx Ward Security", 60, 5, "Routine corporate district patrols. Elevated alert after the explosion.", System.currentTimeMillis(), true),
            Faction("neon_coil", "Neon Coil Syndicate", 40, 15, "Profiting from the chaos. Running illegal auctions in the entertainment district.", System.currentTimeMillis(), true)
        )
        
        val activeNodes = listOf("silas_rescue", "package_retrieval")

        val initialBoard = WorldBoardEntity(
            id = "MAIN",
            factions = defaultFactions,
            activeNodes = activeNodes,
            causalLog = emptyList(),
            lastTickTimestamp = System.currentTimeMillis(),
            pendingMorningBrief = ""
        )
        worldBoardDao.upsert(initialBoard)
    }

    override suspend fun appendCausalEvents(newEvents: List<CausalEvent>) {
        val currentBoard = getWorldBoardOnce()
        val updatedLog = (currentBoard.causalLog + newEvents).takeLast(50) // rolling window max 50
        worldBoardDao.upsert(currentBoard.copy(causalLog = updatedLog))
    }

    override suspend fun updateLastTickTimestamp(timestamp: Long) {
        val currentBoard = getWorldBoardOnce()
        worldBoardDao.upsert(currentBoard.copy(lastTickTimestamp = timestamp))
    }

    override suspend fun applyTickResponse(response: WorldTickResponse) {
        val currentBoard = getWorldBoardOnce()
        val now = System.currentTimeMillis()
        
        val updatedFactions = currentBoard.factions.map { faction ->
            val update = response.factionUpdates.find { it.factionId == faction.id }
            if (update != null) {
                faction.copy(
                    currentAgenda = update.agendaProgress,
                    hostilityToPlayer = (faction.hostilityToPlayer + update.alertDelta).coerceIn(0, 100),
                    lastTickTimestamp = now
                )
            } else {
                faction
            }
        }

        // Apply node resolutions
        val resolvedNodeIds = response.nodeResolutions.filter { it.resolved }.map { it.nodeId }
        val updatedNodes = currentBoard.activeNodes.filter { it !in resolvedNodeIds }

        // Also queue any new opportunities? The prompt mentions newOpportunities. 
        // We add them to activeNodes.
        val finalNodes = (updatedNodes + response.newOpportunities).distinct()

        worldBoardDao.upsert(
            currentBoard.copy(
                factions = updatedFactions,
                activeNodes = finalNodes,
                lastTickTimestamp = now,
                pendingMorningBrief = response.morningBrief
            )
        )
    }

    override suspend fun clearPendingMorningBrief() {
        val currentBoard = getWorldBoardOnce()
        worldBoardDao.upsert(currentBoard.copy(pendingMorningBrief = ""))
    }
}
