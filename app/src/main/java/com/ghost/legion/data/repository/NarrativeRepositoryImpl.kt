package com.ghost.legion.data.repository

import com.ghost.legion.data.local.dao.ChatLogDao
import com.ghost.legion.data.local.dao.ActiveStateDao
import com.ghost.legion.data.local.dao.WorldBoardDao
import com.ghost.legion.data.local.entity.ChatLogEntity
import com.ghost.legion.data.remote.GeminiClient
import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.NarrativeEntity
import com.ghost.legion.domain.model.NarrativeResponse
import com.ghost.legion.domain.model.NarrativeUiData
import com.ghost.legion.domain.repository.NarrativeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NarrativeRepositoryImpl @Inject constructor(
    private val chatLogDao: ChatLogDao,
    private val activeStateDao: ActiveStateDao,
    private val worldBoardDao: WorldBoardDao,
    private val geminiClient: GeminiClient
) : NarrativeRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun getChatHistory(sessionId: String): Flow<List<ChatMessage>> {
        return chatLogDao.getAllMessages(sessionId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun sendMessage(sessionId: String, playerMessage: String): NarrativeResponse {
        // Save player message
        chatLogDao.insert(
            ChatLogEntity(
                sessionId = sessionId,
                entity = NarrativeEntity.DEVON.name,
                messageText = playerMessage,
                isPlayerMessage = true
            )
        )

        // Get context
        val gameState = activeStateDao.getStateOnce()?.let { entity ->
            com.ghost.legion.domain.model.GameState(
                playerName = entity.playerName,
                currentLocation = entity.currentLocation,
                inventory = try { json.decodeFromString(entity.inventoryJson) } catch (e: Exception) { emptyList() },
                powerLevel = entity.powerLevel,
                echoIntegrated = entity.echoIntegrated,
                auraRelationship = entity.auraRelationship,
                echoRelationship = entity.echoRelationship,
                moralityScore = entity.moralityScore,
                currentActId = entity.currentActId,
                currentChapterId = entity.currentChapterId
            )
        } ?: com.ghost.legion.domain.model.GameState()

        val factions = worldBoardDao.getAllFactions().first().map { it.toDomainFaction() }
        val chatHistory = chatLogDao.getAllMessages(sessionId).first().map { it.toDomain() }

        // Generate response
        val response = geminiClient.generateNarrative(gameState, factions, chatHistory, playerMessage)

        // Save AI response
        chatLogDao.insert(
            ChatLogEntity(
                sessionId = sessionId,
                entity = response.entity,
                messageText = response.textResponse,
                uiDataJson = response.uiData?.let { json.encodeToString(it) }
            )
        )

        return response
    }

    override suspend fun sendChoice(sessionId: String, choiceId: String): NarrativeResponse {
        return sendMessage(sessionId, "[CHOICE: $choiceId]")
    }

    override suspend fun sendTriadVote(sessionId: String, votedPosition: String): NarrativeResponse {
        return sendMessage(sessionId, "[TRIAD_VOTE: $votedPosition]")
    }

    override suspend fun startNewSession(): String {
        return UUID.randomUUID().toString()
    }

    override suspend fun getMessageCount(sessionId: String): Int {
        return chatLogDao.getMessageCount(sessionId)
    }

    private fun ChatLogEntity.toDomain(): ChatMessage {
        val uiData = uiDataJson?.let {
            try { json.decodeFromString<NarrativeUiData>(it) } catch (e: Exception) { null }
        }
        return ChatMessage(
            id = id,
            sessionId = sessionId,
            entity = NarrativeEntity.fromString(entity),
            text = messageText,
            uiData = uiData,
            timestamp = timestamp,
            isPlayerMessage = isPlayerMessage
        )
    }

    private fun com.ghost.legion.data.local.entity.WorldBoardEntity.toDomainFaction(): Faction {
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
