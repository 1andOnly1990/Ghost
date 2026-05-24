package com.ghost.legion.data.repository

import com.ghost.legion.data.local.dao.ActiveStateDao
import com.ghost.legion.data.local.dao.WorldBoardDao
import com.ghost.legion.data.local.entity.WorldBoardEntity
import com.ghost.legion.data.remote.GeminiClient
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.GameState
import com.ghost.legion.domain.repository.WorldRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
private data class FactionTickResult(
    @SerialName("faction_id") val factionId: String,
    @SerialName("new_agenda") val newAgenda: String,
    @SerialName("influence_delta") val influenceDelta: Int = 0,
    @SerialName("hostility_delta") val hostilityDelta: Int = 0
)

@Singleton
class WorldRepositoryImpl @Inject constructor(
    private val worldBoardDao: WorldBoardDao,
    private val activeStateDao: ActiveStateDao,
    private val geminiClient: GeminiClient
) : WorldRepository {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    override fun getAllFactions(): Flow<List<Faction>> {
        return worldBoardDao.getAllFactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getHostileFactions(): Flow<List<Faction>> {
        return worldBoardDao.getHostileFactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun initializeWorldBoard() {
        if (worldBoardDao.getCount() > 0) return

        val defaultFactions = listOf(
            WorldBoardEntity(
                factionId = "black_sun",
                factionName = "Black Sun Solutions",
                influence = 70,
                hostilityToPlayer = 10,
                currentAgenda = "Searching for the lost courier package. Standard recovery protocols in effect."
            ),
            WorldBoardEntity(
                factionId = "pantheon",
                factionName = "The Pantheon",
                influence = 95,
                hostilityToPlayer = 0,
                currentAgenda = "Maintaining equilibrium in Veridia City. Business as usual."
            ),
            WorldBoardEntity(
                factionId = "couriers",
                factionName = "Independent Couriers Guild",
                influence = 25,
                hostilityToPlayer = 0,
                currentAgenda = "Keeping the network alive. Silas Vane's disappearance has people spooked."
            ),
            WorldBoardEntity(
                factionId = "onyx_ward",
                factionName = "Onyx Ward Security",
                influence = 60,
                hostilityToPlayer = 5,
                currentAgenda = "Routine corporate district patrols. Elevated alert after the explosion."
            ),
            WorldBoardEntity(
                factionId = "neon_coil",
                factionName = "Neon Coil Syndicate",
                influence = 40,
                hostilityToPlayer = 15,
                currentAgenda = "Profiting from the chaos. Running illegal auctions in the entertainment district."
            )
        )
        worldBoardDao.upsertAll(defaultFactions)
    }

    override suspend fun processFactionTick() {
        if (!geminiClient.isInitialized()) return

        val factions = worldBoardDao.getAllFactions().first().map { it.toDomain() }
        if (factions.isEmpty()) return

        val stateEntity = activeStateDao.getStateOnce() ?: return
        val gameState = GameState(
            playerName = stateEntity.playerName,
            currentLocation = stateEntity.currentLocation,
            powerLevel = stateEntity.powerLevel,
            moralityScore = stateEntity.moralityScore
        )

        val lastTick = factions.minOfOrNull { it.lastTickTimestamp } ?: System.currentTimeMillis()
        val hoursSinceLastTick = (System.currentTimeMillis() - lastTick) / (1000 * 60 * 60)

        try {
            val tickResultJson = geminiClient.generateFactionTick(gameState, factions, hoursSinceLastTick)
            val tickResults = json.decodeFromString<List<FactionTickResult>>(tickResultJson)

            val now = System.currentTimeMillis()
            for (result in tickResults) {
                val existing = worldBoardDao.getFaction(result.factionId) ?: continue
                worldBoardDao.upsert(
                    existing.copy(
                        influence = (existing.influence + result.influenceDelta).coerceIn(0, 100),
                        hostilityToPlayer = (existing.hostilityToPlayer + result.hostilityDelta).coerceIn(0, 100),
                        currentAgenda = result.newAgenda,
                        lastTickTimestamp = now
                    )
                )
            }
        } catch (e: Exception) {
            // Faction ticks are best-effort; if the LLM call fails, world stays static until next tick
            e.printStackTrace()
        }
    }

    private fun WorldBoardEntity.toDomain(): Faction {
        return Faction(
            id = factionId,
            name = factionName,
            influence = influence,
            hostilityToPlayer = hostilityToPlayer,
            currentAgenda = currentAgenda,
            lastTickTimestamp = lastTickTimestamp,
            isActive = isActive
        )
    }
}
