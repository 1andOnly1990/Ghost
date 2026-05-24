package com.ghost.legion.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CausalEvent(
    val timestamp: Long,
    val trigger: CausalTrigger,
    val description: String,
    val factionAffected: String? = null,
    val nodeAffected: String? = null
)

@Serializable
enum class CausalTrigger {
    PLAYER_ACTION,      // Devon did something
    PLAYER_INACTION,    // Devon chose not to, or went offline
    FACTION_RESPONSE,   // Faction reacted to Devon
    TIME_PASSAGE,       // Tick-driven: time simply passed
    SERENDIPITY,        // The lottery — something happened with no cause
    COLLATERAL          // Butterfly effect from an unrelated Devon action
}
