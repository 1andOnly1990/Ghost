package com.ghost.legion.data.remote

import com.ghost.legion.domain.model.*
import com.ghost.legion.domain.repository.GenerativeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import kotlinx.serialization.encodeToString
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OllamaClient @Inject constructor() : GenerativeClient {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private var initialized = false

    override fun initialize(apiKey: String) {
        // apiKey is ignored for local Ollama
        initialized = true
    }

    override fun isInitialized(): Boolean = initialized

    override suspend fun generateNarrative(
        gameState: GameState,
        factions: List<Faction>,
        chatHistory: List<ChatMessage>,
        playerInput: String
    ): NarrativeResponse = withContext(Dispatchers.IO) {
        val prompt = Prompts.buildPrompt(gameState, factions, chatHistory, playerInput)
        
        val requestJson = buildJsonObject {
            put("model", "llama3")
            put("prompt", Prompts.SYSTEM_PROMPT + "\n\n" + prompt)
            put("format", "json")
            put("stream", false)
        }

        val responseText = makeRequest(requestJson.toString())
        json.decodeFromString<NarrativeResponse>(responseText)
    }

    override suspend fun runWorldSimulation(payload: WorldSimulationPayload): WorldTickResponse = withContext(Dispatchers.IO) {
        val promptText = json.encodeToString(payload)
        
        val requestJson = buildJsonObject {
            put("model", "llama3")
            put("prompt", Prompts.WORLD_SIMULATION_PROMPT + "\n\n" + promptText)
            put("format", "json")
            put("stream", false)
        }

        val responseText = makeRequest(requestJson.toString())
        json.decodeFromString<WorldTickResponse>(responseText)
    }

    private fun makeRequest(payload: String): String {
        // 10.0.2.2 is the special alias to your host loopback interface (127.0.0.1) on the Android Emulator
        val url = URL("http://10.0.2.2:11434/api/generate")
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            connection.outputStream.use { os ->
                val input = payload.toByteArray(Charsets.UTF_8)
                os.write(input, 0, input.size)
            }

            val status = connection.responseCode
            if (status !in 200..299) {
                val errorStream = connection.errorStream?.bufferedReader()?.use { it.readText() }
                throw IllegalStateException("Ollama API Error: ${"$"}status ${"$"}errorStream")
            }

            val responseJsonText = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonResponse = json.parseToJsonElement(responseJsonText).jsonObject
            val actualResponseText = jsonResponse["response"]?.jsonPrimitive?.content ?: "{}"
            
            return actualResponseText
        } finally {
            connection.disconnect()
        }
    }
}
