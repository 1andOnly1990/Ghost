package com.ghost.legion.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ghost.legion.data.local.entity.ChatLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatLogDao {
    @Query("SELECT * FROM chat_log WHERE sessionId = :sessionId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMessages(sessionId: String, limit: Int = 50): Flow<List<ChatLogEntity>>

    @Query("SELECT * FROM chat_log ORDER BY timestamp DESC LIMIT :limit")
    fun getGlobalRecentMessages(limit: Int): Flow<List<ChatLogEntity>>

    @Query("SELECT * FROM chat_log WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getAllMessages(sessionId: String): Flow<List<ChatLogEntity>>

    @Insert
    suspend fun insert(message: ChatLogEntity)

    @Insert
    suspend fun insertAll(messages: List<ChatLogEntity>)

    @Query("SELECT COUNT(*) FROM chat_log WHERE sessionId = :sessionId")
    suspend fun getMessageCount(sessionId: String): Int

    @Query("DELETE FROM chat_log WHERE sessionId = :sessionId")
    suspend fun clearSession(sessionId: String)

    @Query("SELECT DISTINCT sessionId FROM chat_log ORDER BY timestamp DESC")
    fun getAllSessionIds(): Flow<List<String>>
}
