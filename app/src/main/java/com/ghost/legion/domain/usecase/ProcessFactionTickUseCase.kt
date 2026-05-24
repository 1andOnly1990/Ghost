package com.ghost.legion.domain.usecase

import com.ghost.legion.domain.repository.WorldRepository
import javax.inject.Inject

class ProcessFactionTickUseCase @Inject constructor(
    private val worldRepository: WorldRepository
) {
    suspend operator fun invoke() {
        worldRepository.processFactionTick()
    }
}
