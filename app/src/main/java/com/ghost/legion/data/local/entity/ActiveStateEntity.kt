package com.ghost.legion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_state")
data class ActiveStateEntity(
    @PrimaryKey val id: Int = 0,
    val playerName: String = "Devon",
    val currentLocation: String = "service_alley",
    val inventoryJson: String = "[]",
    val powerLevel: Int = 1,
    val echoIntegrated: Boolean = false,
    val auraRelationship: Int = 50,
    val echoRelationship: Int = 0,
    val moralityScore: Int = 50,
    val currentActId: String = "act_1",
    val currentChapterId: String = "chapter_1",
    val lastPlayedTimestamp: Long = System.currentTimeMillis()
)
