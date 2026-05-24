package com.ghost.legion.domain.model

enum class NarrativeTone {
    HUMOR,
    TENSION,
    COMBAT,
    MORAL_DILEMMA,
    REFLECTIVE;

    companion object {
        fun fromString(value: String): NarrativeTone {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: HUMOR
        }
    }
}
