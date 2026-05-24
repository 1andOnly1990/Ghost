package com.ghost.legion.di

import com.ghost.legion.data.repository.GameStateRepositoryImpl
import com.ghost.legion.data.repository.NarrativeRepositoryImpl
import com.ghost.legion.data.repository.WorldRepositoryImpl
import com.ghost.legion.domain.repository.GameStateRepository
import com.ghost.legion.domain.repository.NarrativeRepository
import com.ghost.legion.domain.repository.WorldRepository
import com.ghost.legion.domain.repository.SettingsRepository
import com.ghost.legion.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameStateRepository(
        impl: GameStateRepositoryImpl
    ): GameStateRepository

    @Binds
    @Singleton
    abstract fun bindNarrativeRepository(
        impl: NarrativeRepositoryImpl
    ): NarrativeRepository

    @Binds
    @Singleton
    abstract fun bindWorldRepository(
        impl: WorldRepositoryImpl
    ): WorldRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
