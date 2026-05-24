package com.ghost.legion.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_log",
    indices = [Index(value = ["sessionId", "timestamp"])]
)
data class ChatLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val entity: String,
    val messageText: String,
    val uiDataJson: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isPlayerMessage: Boolean = false
)
