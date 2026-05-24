package com.ghost.legion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world_board")
data class WorldBoardEntity(
    @PrimaryKey val factionId: String,
    val factionName: String,
    val influence: Int = 50,
    val hostilityToPlayer: Int = 0,
    val currentAgenda: String = "",
    val lastTickTimestamp: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
