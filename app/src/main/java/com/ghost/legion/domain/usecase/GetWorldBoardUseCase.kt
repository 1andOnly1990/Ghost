package com.ghost.legion.domain.usecase

import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.repository.WorldRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorldBoardUseCase @Inject constructor(
    private val worldRepository: WorldRepository
) {
    operator fun invoke(): Flow<List<Faction>> {
        return worldRepository.getAllFactions()
    }
}
