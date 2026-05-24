package com.ghost.legion.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorldTickResponse(
    val factionUpdates: List<FactionUpdate>,
    val nodeResolutions: List<NodeResolution>,
    val morningBrief: String,           // Plain text for the notification
    val briefTitle: String,             // Short notification headline
    val newOpportunities: List<String>  // Doors Devon's inaction accidentally opened
)

@Serializable
data class FactionUpdate(
    val factionId: String,
    val newStatus: String,
    val locationChange: String? = null,
    val alertDelta: Int = 0,            // -2 to +2; relaxed to escalated
    val agendaProgress: String,         // Human-readable: "Convoy arrived. Package secured."
    val devonCaused: Boolean            // true = Devon's action triggered this
)

@Serializable
data class NodeResolution(
    val nodeId: String,                 // e.g. "silas_rescue", "package_retrieval"
    val resolved: Boolean,
    val resolvedBy: String,             // "PLAYER", "FACTION", "TIME", "LUCK"
    val repackagedAs: String? = null,   // Kaleidoscope: new entry point if missed
    val burned: Boolean                 // true = permanently closed, never loops
)
