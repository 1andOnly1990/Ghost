package com.ghost.legion.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ghost.legion.data.local.entity.WorldBoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldBoardDao {
    @Query("SELECT * FROM world_board WHERE isActive = 1 ORDER BY influence DESC")
    fun getAllFactions(): Flow<List<WorldBoardEntity>>

    @Query("SELECT * FROM world_board WHERE factionId = :factionId")
    suspend fun getFaction(factionId: String): WorldBoardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(faction: WorldBoardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(factions: List<WorldBoardEntity>)

    @Query("UPDATE world_board SET currentAgenda = :agenda, lastTickTimestamp = :timestamp WHERE factionId = :factionId")
    suspend fun updateAgenda(factionId: String, agenda: String, timestamp: Long)

    @Query("UPDATE world_board SET hostilityToPlayer = :hostility WHERE factionId = :factionId")
    suspend fun updateHostility(factionId: String, hostility: Int)

    @Query("SELECT * FROM world_board WHERE hostilityToPlayer >= :threshold AND isActive = 1")
    fun getHostileFactions(threshold: Int = 70): Flow<List<WorldBoardEntity>>

    @Query("SELECT COUNT(*) FROM world_board")
    suspend fun getCount(): Int
}
