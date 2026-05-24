package com.ghost.legion.domain.model

data class GameState(
    val playerName: String = "Devon",
    val currentLocation: String = "service_alley",
    val inventory: List<String> = emptyList(),
    val powerLevel: Int = 1,
    val echoIntegrated: Boolean = false,
    val auraRelationship: Int = 50,
    val echoRelationship: Int = 0,
    val moralityScore: Int = 50,
    val currentActId: String = "act_1",
    val currentChapterId: String = "chapter_1"
) {
    val powerTier: String
        get() = when (powerLevel) {
            1 -> "Street"
            2 -> "City"
            3 -> "God"
            else -> "Unknown"
        }
}
