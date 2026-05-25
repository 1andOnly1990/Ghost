package com.ghost.legion.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.content
import com.ghost.legion.domain.model.GameState
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.NarrativeResponse
import com.ghost.legion.domain.model.WorldSimulationPayload
import com.ghost.legion.domain.model.WorldTickResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import com.ghost.legion.domain.repository.GenerativeClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiClient @Inject constructor() : GenerativeClient {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private var model: GenerativeModel? = null
    private var apiKey: String? = null

    override fun initialize(apiKey: String) {
        this.apiKey = apiKey
        model = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey,
            generationConfig = GenerationConfig.builder().apply {
                responseMimeType = "application/json"
                temperature = 0.9f
                topP = 0.95f
                maxOutputTokens = 2048
            }.build(),
            systemInstruction = content { text(com.ghost.legion.domain.model.Prompts.SYSTEM_PROMPT) }
        )
    }

    override fun isInitialized(): Boolean = model != null

    override suspend fun generateNarrative(
        gameState: GameState,
        factions: List<Faction>,
        chatHistory: List<ChatMessage>,
        playerInput: String
    ): NarrativeResponse {
        val activeModel = model ?: throw IllegalStateException("GeminiClient not initialized. Set API key first.")

        val contextPrompt = com.ghost.legion.domain.model.Prompts.buildPrompt(gameState, factions, chatHistory, playerInput)
        val response = activeModel.generateContent(contextPrompt)
        val responseText = response.text ?: throw IllegalStateException("Empty response from Gemini")

        return json.decodeFromString<NarrativeResponse>(responseText)
    }

    override suspend fun runWorldSimulation(payload: WorldSimulationPayload): WorldTickResponse {
        val key = apiKey ?: throw IllegalStateException("GeminiClient not initialized.")
        val simModel = GenerativeModel(
            modelName = "gemini-2.0-flash-lite",
            apiKey = key,
            generationConfig = GenerationConfig.builder().apply {
                responseMimeType = "application/json"
                temperature = 0.4f
            }.build(),
            systemInstruction = content { text(com.ghost.legion.domain.model.Prompts.WORLD_SIMULATION_PROMPT) }
        )

        val promptText = json.encodeToString(payload)
        val response = simModel.generateContent(promptText)
        val responseText = response.text ?: "{}"
        return json.decodeFromString<WorldTickResponse>(responseText)
    }

    companion object
}
