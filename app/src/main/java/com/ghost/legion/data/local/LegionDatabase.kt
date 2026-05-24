package com.ghost.legion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ghost.legion.data.local.converter.Converters
import com.ghost.legion.data.local.dao.ActiveStateDao
import com.ghost.legion.data.local.dao.ChatLogDao
import com.ghost.legion.data.local.dao.WorldBoardDao
import com.ghost.legion.data.local.entity.ActiveStateEntity
import com.ghost.legion.data.local.entity.ChatLogEntity
import com.ghost.legion.data.local.entity.WorldBoardEntity

@Database(
    entities = [
        ActiveStateEntity::class,
        WorldBoardEntity::class,
        ChatLogEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LegionDatabase : RoomDatabase() {
    abstract fun activeStateDao(): ActiveStateDao
    abstract fun worldBoardDao(): WorldBoardDao
    abstract fun chatLogDao(): ChatLogDao
}
