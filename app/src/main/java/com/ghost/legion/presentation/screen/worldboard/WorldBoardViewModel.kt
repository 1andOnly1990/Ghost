package com.ghost.legion.presentation.screen.worldboard

import androidx.lifecycle.ViewModel
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.usecase.GetWorldBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WorldBoardViewModel @Inject constructor(
    getWorldBoardUseCase: GetWorldBoardUseCase
) : ViewModel() {
    val factions: Flow<List<Faction>> = getWorldBoardUseCase()
}
