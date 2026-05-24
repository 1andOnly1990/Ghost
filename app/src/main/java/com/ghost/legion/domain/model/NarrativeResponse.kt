package com.ghost.legion.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NarrativeResponse(
    val entity: String,
    @SerialName("text_response")
    val textResponse: String,
    @SerialName("ui_data")
    val uiData: NarrativeUiData? = null
)

@Serializable
data class NarrativeUiData(
    val tone: String = "HUMOR",
    val location: String? = null,
    val choices: List<NarrativeChoice> = emptyList(),
    @SerialName("triad_vote")
    val triadVote: TriadVoteData? = null,
    @SerialName("state_changes")
    val stateChanges: StateChanges? = null
)

@Serializable
data class NarrativeChoice(
    val id: String,
    val text: String,
    @SerialName("risk_level")
    val riskLevel: String = "LOW"
)

@Serializable
data class TriadVoteData(
    val question: String,
    @SerialName("devon_position")
    val devonPosition: String,
    @SerialName("aura_position")
    val auraPosition: String,
    @SerialName("echo_position")
    val echoPosition: String? = null
)

@Serializable
data class StateChanges(
    @SerialName("morality_delta")
    val moralityDelta: Int? = null,
    @SerialName("aura_relationship_delta")
    val auraRelationshipDelta: Int? = null,
    @SerialName("echo_relationship_delta")
    val echoRelationshipDelta: Int? = null,
    @SerialName("new_items")
    val newItems: List<String>? = null,
    @SerialName("removed_items")
    val removedItems: List<String>? = null,
    @SerialName("location_change")
    val locationChange: String? = null,
    @SerialName("power_level_change")
    val powerLevelChange: Int? = null
)
