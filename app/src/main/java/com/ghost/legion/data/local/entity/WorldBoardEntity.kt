package com.ghost.legion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ghost.legion.domain.model.CausalEvent
import com.ghost.legion.domain.model.Faction

@Entity(tableName = "world_board")
data class WorldBoardEntity(
    @PrimaryKey val id: String = "MAIN",
    val factions: List<Faction> = emptyList(),
    val activeNodes: List<String> = emptyList(),
    val causalLog: List<CausalEvent> = emptyList(),
    val lastTickTimestamp: Long = System.currentTimeMillis(),
    val pendingMorningBrief: String = ""
)
