package com.ghost.legion.domain.model

enum class NarrativeEntity(val displayName: String) {
    DEVON("Devon"),
    AURA("Aura"),
    ECHO("Echo"),
    LEGION("Legion"),
    SYSTEM("System");

    companion object {
        fun fromString(value: String): NarrativeEntity {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SYSTEM
        }
    }
}
