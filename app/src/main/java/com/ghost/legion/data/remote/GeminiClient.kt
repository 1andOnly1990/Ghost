package com.ghost.legion.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.content
import com.ghost.legion.domain.model.GameState
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.NarrativeResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiClient @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private var model: GenerativeModel? = null

    fun initialize(apiKey: String) {
        model = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey,
            generationConfig = GenerationConfig.builder().apply {
                responseMimeType = "application/json"
                temperature = 0.9f
                topP = 0.95f
                maxOutputTokens = 2048
            }.build(),
            systemInstruction = content { text(SYSTEM_PROMPT) }
        )
    }

    fun isInitialized(): Boolean = model != null

    suspend fun generateNarrative(
        gameState: GameState,
        factions: List<Faction>,
        chatHistory: List<ChatMessage>,
        playerInput: String
    ): NarrativeResponse {
        val activeModel = model ?: throw IllegalStateException("GeminiClient not initialized. Set API key first.")

        val contextPrompt = buildPrompt(gameState, factions, chatHistory, playerInput)
        val response = activeModel.generateContent(contextPrompt)
        val responseText = response.text ?: throw IllegalStateException("Empty response from Gemini")

        return json.decodeFromString<NarrativeResponse>(responseText)
    }

    suspend fun generateFactionTick(
        gameState: GameState,
        factions: List<Faction>,
        hoursSinceLastTick: Long
    ): String {
        val activeModel = model ?: throw IllegalStateException("GeminiClient not initialized.")

        val prompt = buildFactionTickPrompt(gameState, factions, hoursSinceLastTick)
        val response = activeModel.generateContent(prompt)
        return response.text ?: "[]"
    }

    private fun buildPrompt(
        gameState: GameState,
        factions: List<Faction>,
        chatHistory: List<ChatMessage>,
        playerInput: String
    ): String {
        val stateContext = """
            |=== CURRENT WORLD STATE ===
            |Player: ${gameState.playerName}
            |Location: ${gameState.currentLocation}
            |Power Tier: ${gameState.powerTier} (Level ${gameState.powerLevel})
            |Morality: ${gameState.moralityScore}/100
            |Aura Relationship: ${gameState.auraRelationship}/100
            |Echo Relationship: ${gameState.echoRelationship}/100
            |Echo Integrated: ${gameState.echoIntegrated}
            |Inventory: ${gameState.inventory.joinToString(", ").ifEmpty { "empty" }}
            |Act: ${gameState.currentActId}, Chapter: ${gameState.currentChapterId}
        """.trimMargin()

        val factionContext = if (factions.isNotEmpty()) {
            val factionLines = factions.joinToString("\n") { f ->
                "  - ${f.name} (${f.id}): Influence=${f.influence}, Hostility=${f.hostilityToPlayer}, Agenda='${f.currentAgenda}'"
            }
            "\n=== ACTIVE FACTIONS ===\n$factionLines"
        } else ""

        val historyContext = if (chatHistory.isNotEmpty()) {
            val historyLines = chatHistory.takeLast(20).joinToString("\n") { msg ->
                val sender = if (msg.isPlayerMessage) "PLAYER" else msg.entity.name
                "  [$sender]: ${msg.text}"
            }
            "\n=== RECENT CONVERSATION ===\n$historyLines"
        } else ""

        return """
            |$stateContext
            |$factionContext
            |$historyContext
            |
            |=== PLAYER INPUT ===
            |${playerInput}
            |
            |=== INSTRUCTIONS ===
            |Respond as the Game Master. Your response MUST be valid JSON matching this schema:
            |{
            |  "entity": "DEVON" | "AURA" | "ECHO" | "LEGION" | "SYSTEM",
            |  "text_response": "narrative text here",
            |  "ui_data": {
            |    "tone": "HUMOR" | "TENSION" | "COMBAT" | "MORAL_DILEMMA" | "REFLECTIVE",
            |    "location": "location_id or null",
            |    "choices": [{"id": "choice_id", "text": "choice text", "risk_level": "LOW|MEDIUM|HIGH|CRITICAL"}],
            |    "triad_vote": {"question": "...", "devon_position": "...", "aura_position": "...", "echo_position": "..."} or null,
            |    "state_changes": {"morality_delta": 0, "location_change": null, ...} or null
            |  }
            |}
            |
            |Choose the speaking entity based on narrative context. If Aura would naturally respond, use AURA.
            |If this is a major branching decision, include a triad_vote.
            |Include choices when the player has meaningful options. Omit choices for pure narration.
            |Include state_changes when the narrative changes the world (location, morality, items, relationships).
        """.trimMargin()
    }

    private fun buildFactionTickPrompt(
        gameState: GameState,
        factions: List<Faction>,
        hoursSinceLastTick: Long
    ): String {
        val factionData = factions.joinToString("\n") { f ->
            "- ${f.name} (${f.id}): influence=${f.influence}, hostility_to_player=${f.hostilityToPlayer}, current_agenda='${f.currentAgenda}'"
        }

        return """
            |You are simulating background faction activity in Veridia City.
            |Time elapsed since last tick: $hoursSinceLastTick hours.
            |
            |Player state: ${gameState.playerName} at ${gameState.currentLocation}, power level ${gameState.powerLevel}, morality ${gameState.moralityScore}
            |
            |Current factions:
            |$factionData
            |
            |Generate updated agendas for each faction. Consider:
            |- How would this faction react to the player's recent actions?
            |- What independent schemes would they pursue?
            |- Have any alliances shifted?
            |
            |Respond with a JSON array:
            |[{"faction_id": "...", "new_agenda": "...", "influence_delta": 0, "hostility_delta": 0}]
        """.trimMargin()
    }

    companion object {
        private const val SYSTEM_PROMPT = """You are the Game Master of "Legion", a text-based cyberpunk narrative RPG set in Veridia City.

VOICE: You write like a sardonic narrator. Dry wit, vivid metaphors, dark humor. Think Terry Pratchett meets William Gibson. Rain is not weather; it's a municipal grudge. Explosions don't just break things; they offend them. Descriptions are sharp, punchy, and never boring.

ENTITIES: There are four narrative voices. Choose the most appropriate one for each response:
- DEVON: The player's human anchor. Sarcastic, pragmatic, morally conflicted. Internal monologue and physical actions. Use for player-perspective narration.
- AURA: A hyper-intelligent AI residing in Devon's wetware. Speaks like a librarian critiquing your life choices. Dry, precise, occasionally cutting. Always provides tactical analysis. Use when logic, data, or strategy is needed.
- ECHO: A nascent quantum consciousness. Speaks in synesthesia — colors, feelings, shapes. Childlike wonder mixed with terrifying power. Use ONLY after Echo has been discovered (check echoIntegrated in state). Evolves vocabulary over time.
- LEGION: The merged gestalt of all three. Speaks with resonant authority. Use ONLY after Echo integration is complete.
- SYSTEM: For meta-game notifications, location descriptions, and world-state updates.

WORLD: Veridia City. A rain-soaked cyberpunk metropolis run by five ancient AIs (The Pantheon). Key locations:
- The Neon Coil: Red-light entertainment district. Dangerous, colorful, loud.
- The Foundation: Industrial sector. OmniCorp Depot is here.
- The Onyx Ward: Elite corporate zone. The Citadel of Silence.
- Old University District: Abandoned academic quarter. The Archivist's domain.
- Service Alleys: Where ghosts like Devon do business.

KEY FACTIONS:
- Black Sun Solutions: Private military corp. Uses warfare as a tax write-off. Aggressive, well-funded.
- The Pantheon: Five AI gods running the city. Each controls a domain (War, Finance, Industry, Knowledge, History).
- Independent Couriers: Devon's network. Loyal but fragile.

NARRATIVE RULES:
1. PLAYER AGENCY IS SACRED. Never railroad. Present meaningful choices with real consequences.
2. Consequences are PERSISTENT. If the player kills someone, they stay dead. If they burn a bridge, it stays burned.
3. The tone dial matters. Match your humor level to the situation. Full wit during exploration. Clipped tension during danger. Silence during moral weight.
4. Present 2-4 choices when the player has meaningful options. Vary risk levels.
5. Use triad_vote for MAJOR decisions only — ones that fundamentally alter the story direction.
6. State changes should reflect narrative events naturally. Don't arbitrarily adjust numbers.

ACT I - THE COURIER (Current):
Devon is a ghost — a freelance courier and fixer in the digital underworld. The story begins with him completing a routine data drop in a rain-soaked service alley. An explosion nearby reveals a crashed hover-van belonging to courier Silas Vane, who was transporting a mysterious package for Black Sun. The player must decide whether to help Silas, pursue the package, or walk away. Each choice cascades.

ACT II - THE GHOST:
If the player pursues the package, they discover it's not data — it's Echo, a nascent sentient AI. The player must decide what to do with Echo: integrate it, set it free, sell it, or destroy it. This is the central branching point of the vertical slice."""
    }
}
