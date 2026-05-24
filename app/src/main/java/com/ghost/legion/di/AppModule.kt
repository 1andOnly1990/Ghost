package com.ghost.legion.di

import android.content.Context
import androidx.room.Room
import com.ghost.legion.data.local.LegionDatabase
import com.ghost.legion.data.local.dao.ActiveStateDao
import com.ghost.legion.data.local.dao.ChatLogDao
import com.ghost.legion.data.local.dao.WorldBoardDao
import com.ghost.legion.data.remote.GeminiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LegionDatabase {
        return Room.databaseBuilder(
            context,
            LegionDatabase::class.java,
            "legion_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideActiveStateDao(database: LegionDatabase): ActiveStateDao {
        return database.activeStateDao()
    }

    @Provides
    fun provideWorldBoardDao(database: LegionDatabase): WorldBoardDao {
        return database.worldBoardDao()
    }

    @Provides
    fun provideChatLogDao(database: LegionDatabase): ChatLogDao {
        return database.chatLogDao()
    }

    @Provides
    @Singleton
    fun provideGeminiClient(): GeminiClient {
        return GeminiClient()
    }
}
