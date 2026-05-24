package com.ghost.legion.domain.repository

import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.NarrativeResponse
import kotlinx.coroutines.flow.Flow

interface NarrativeRepository {
    fun getChatHistory(sessionId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(sessionId: String, playerMessage: String): NarrativeResponse
    suspend fun sendChoice(sessionId: String, choiceId: String): NarrativeResponse
    suspend fun sendTriadVote(sessionId: String, votedPosition: String): NarrativeResponse
    suspend fun startNewSession(): String
    suspend fun getMessageCount(sessionId: String): Int
    fun getRecentChatHistory(limit: Int): Flow<List<com.ghost.legion.data.local.entity.ChatLogEntity>>
    suspend fun injectSystemMessage(sessionId: String, text: String, entityName: String, visualState: com.ghost.legion.domain.model.VisualState)
}
