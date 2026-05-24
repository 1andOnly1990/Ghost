package com.ghost.legion.domain.repository

import com.ghost.legion.domain.model.Faction
import kotlinx.coroutines.flow.Flow

interface WorldRepository {
    fun getAllFactions(): Flow<List<Faction>>
    fun getHostileFactions(): Flow<List<Faction>>
    suspend fun initializeWorldBoard()
    suspend fun processFactionTick()
}
