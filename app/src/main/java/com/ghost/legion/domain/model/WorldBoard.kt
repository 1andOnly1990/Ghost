package com.ghost.legion.domain.model

data class Faction(
    val id: String,
    val name: String,
    val influence: Int,
    val hostilityToPlayer: Int,
    val currentAgenda: String,
    val lastTickTimestamp: Long,
    val isActive: Boolean
) {
    val threatLevel: String
        get() = when {
            hostilityToPlayer >= 80 -> "CRITICAL"
            hostilityToPlayer >= 60 -> "HIGH"
            hostilityToPlayer >= 40 -> "MODERATE"
            hostilityToPlayer >= 20 -> "LOW"
            else -> "NEUTRAL"
        }
}
