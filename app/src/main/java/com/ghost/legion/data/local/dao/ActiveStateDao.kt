package com.ghost.legion.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ghost.legion.data.local.entity.ActiveStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiveStateDao {
    @Query("SELECT * FROM active_state WHERE id = 0")
    fun getState(): Flow<ActiveStateEntity?>

    @Query("SELECT * FROM active_state WHERE id = 0")
    suspend fun getStateOnce(): ActiveStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: ActiveStateEntity)

    @Query("UPDATE active_state SET moralityScore = moralityScore + :delta WHERE id = 0")
    suspend fun updateMorality(delta: Int)

    @Query("UPDATE active_state SET auraRelationship = auraRelationship + :delta WHERE id = 0")
    suspend fun updateAuraRelationship(delta: Int)

    @Query("UPDATE active_state SET echoRelationship = echoRelationship + :delta WHERE id = 0")
    suspend fun updateEchoRelationship(delta: Int)

    @Query("UPDATE active_state SET currentLocation = :location WHERE id = 0")
    suspend fun updateLocation(location: String)

    @Query("UPDATE active_state SET powerLevel = :level WHERE id = 0")
    suspend fun updatePowerLevel(level: Int)

    @Query("UPDATE active_state SET echoIntegrated = :integrated WHERE id = 0")
    suspend fun updateEchoIntegrated(integrated: Boolean)

    @Query("UPDATE active_state SET inventoryJson = :inventoryJson WHERE id = 0")
    suspend fun updateInventory(inventoryJson: String)

    @Query("UPDATE active_state SET lastPlayedTimestamp = :timestamp WHERE id = 0")
    suspend fun updateLastPlayed(timestamp: Long)
}
