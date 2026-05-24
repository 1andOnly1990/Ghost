package com.ghost.legion.domain.model

data class ChatMessage(
    val id: Long = 0,
    val sessionId: String,
    val entity: NarrativeEntity,
    val text: String,
    val uiData: NarrativeUiData? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isPlayerMessage: Boolean = false
)
