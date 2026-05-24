package com.ghost.legion.domain.repository

import com.ghost.legion.data.local.entity.WorldBoardEntity
import com.ghost.legion.domain.model.CausalEvent
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.WorldTickResponse
import kotlinx.coroutines.flow.Flow

interface WorldRepository {
    fun getAllFactions(): Flow<List<Faction>>
    fun getHostileFactions(): Flow<List<Faction>>
    fun getWorldBoardFlow(): Flow<WorldBoardEntity>
    suspend fun getWorldBoardOnce(): WorldBoardEntity
    
    suspend fun initializeWorldBoard()
    suspend fun appendCausalEvents(newEvents: List<CausalEvent>)
    suspend fun updateLastTickTimestamp(timestamp: Long)
    suspend fun applyTickResponse(response: WorldTickResponse)
    suspend fun clearPendingMorningBrief()
}
