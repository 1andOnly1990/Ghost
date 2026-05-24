package com.ghost.legion.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ghost.legion.data.local.entity.WorldBoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldBoardDao {
    @Query("SELECT * FROM world_board WHERE id = 'MAIN'")
    fun getWorldBoardFlow(): Flow<WorldBoardEntity?>

    @Query("SELECT * FROM world_board WHERE id = 'MAIN'")
    suspend fun getWorldBoardOnce(): WorldBoardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(worldBoard: WorldBoardEntity)

    @Query("DELETE FROM world_board")
    suspend fun clear()
}
