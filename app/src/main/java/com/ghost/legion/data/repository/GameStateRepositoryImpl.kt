package com.ghost.legion.data.repository

import com.ghost.legion.data.local.dao.ActiveStateDao
import com.ghost.legion.data.local.entity.ActiveStateEntity
import com.ghost.legion.domain.model.GameState
import com.ghost.legion.domain.repository.GameStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStateRepositoryImpl @Inject constructor(
    private val activeStateDao: ActiveStateDao
) : GameStateRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun getGameState(): Flow<GameState?> {
        return activeStateDao.getState().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getGameStateOnce(): GameState? {
        return activeStateDao.getStateOnce()?.toDomain()
    }

    override suspend fun saveGameState(state: GameState) {
        activeStateDao.upsert(state.toEntity())
    }

    override suspend fun applyStateChanges(
        moralityDelta: Int?,
        auraRelationshipDelta: Int?,
        echoRelationshipDelta: Int?,
        newItems: List<String>?,
        removedItems: List<String>?,
        locationChange: String?,
        powerLevelChange: Int?
    ) {
        moralityDelta?.let { activeStateDao.updateMorality(it) }
        auraRelationshipDelta?.let { activeStateDao.updateAuraRelationship(it) }
        echoRelationshipDelta?.let { activeStateDao.updateEchoRelationship(it) }
        locationChange?.let { activeStateDao.updateLocation(it) }
        powerLevelChange?.let { activeStateDao.updatePowerLevel(it) }

        // Handle inventory changes
        if (newItems != null || removedItems != null) {
            val current = activeStateDao.getStateOnce() ?: return
            val currentInventory = try {
                json.decodeFromString<List<String>>(current.inventoryJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updated = currentInventory
                .plus(newItems ?: emptyList())
                .minus((removedItems ?: emptyList()).toSet())
            activeStateDao.updateInventory(json.encodeToString(updated))
        }

        activeStateDao.updateLastPlayed(System.currentTimeMillis())
    }

    override suspend fun initializeNewGame() {
        activeStateDao.upsert(ActiveStateEntity())
    }

    private fun ActiveStateEntity.toDomain(): GameState {
        val inventory = try {
            json.decodeFromString<List<String>>(inventoryJson)
        } catch (e: Exception) {
            emptyList()
        }
        return GameState(
            playerName = playerName,
            currentLocation = currentLocation,
            inventory = inventory,
            powerLevel = powerLevel,
            echoIntegrated = echoIntegrated,
            auraRelationship = auraRelationship,
            echoRelationship = echoRelationship,
            moralityScore = moralityScore,
            currentActId = currentActId,
            currentChapterId = currentChapterId
        )
    }

    private fun GameState.toEntity(): ActiveStateEntity {
        return ActiveStateEntity(
            playerName = playerName,
            currentLocation = currentLocation,
            inventoryJson = json.encodeToString(inventory),
            powerLevel = powerLevel,
            echoIntegrated = echoIntegrated,
            auraRelationship = auraRelationship,
            echoRelationship = echoRelationship,
            moralityScore = moralityScore,
            currentActId = currentActId,
            currentChapterId = currentChapterId,
            lastPlayedTimestamp = System.currentTimeMillis()
        )
    }
}
