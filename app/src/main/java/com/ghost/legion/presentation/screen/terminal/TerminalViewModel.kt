package com.ghost.legion.presentation.screen.terminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.legion.domain.model.ChatMessage
import com.ghost.legion.domain.model.GameState
import com.ghost.legion.domain.model.NarrativeEntity
import com.ghost.legion.domain.model.NarrativeUiData
import com.ghost.legion.domain.usecase.CastTriadVoteUseCase
import com.ghost.legion.domain.usecase.LoadGameStateUseCase
import com.ghost.legion.domain.usecase.SendMessageUseCase
import com.ghost.legion.domain.repository.NarrativeRepository
import com.ghost.legion.domain.repository.GameStateRepository
import com.ghost.legion.domain.repository.WorldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TerminalUiState(
    val messages: List<ChatMessage> = emptyList(),
    val gameState: GameState = GameState(),
    val activeEntity: NarrativeEntity = NarrativeEntity.DEVON,
    val currentUiData: NarrativeUiData? = null,
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val error: String? = null,
    val sessionId: String = ""
)

@HiltViewModel
class TerminalViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val castTriadVoteUseCase: CastTriadVoteUseCase,
    private val loadGameStateUseCase: LoadGameStateUseCase,
    private val narrativeRepository: NarrativeRepository,
    private val gameStateRepository: GameStateRepository,
    private val worldRepository: WorldRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TerminalUiState())
    val uiState: StateFlow<TerminalUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeGame()
        }
    }

    private suspend fun initializeGame() {
        try {
            // Initialize game state if needed
            if (gameStateRepository.getGameStateOnce() == null) {
                gameStateRepository.initializeNewGame()
            }

            // Initialize world board
            worldRepository.initializeWorldBoard()

            // Start new session
            val sessionId = narrativeRepository.startNewSession()

            // Observe game state
            loadGameStateUseCase().collect { gameState ->
                _uiState.update { it.copy(gameState = gameState) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Failed to initialize: ${e.message}") }
        }
    }

    fun startSession() {
        viewModelScope.launch {
            val sessionId = narrativeRepository.startNewSession()
            _uiState.update {
                it.copy(sessionId = sessionId, isInitialized = true, messages = emptyList())
            }

            // Observe chat history
            narrativeRepository.getChatHistory(sessionId).collect { messages ->
                _uiState.update { state ->
                    val lastEntity = messages.lastOrNull { !it.isPlayerMessage }?.entity
                        ?: NarrativeEntity.DEVON
                    state.copy(
                        messages = messages,
                        activeEntity = lastEntity
                    )
                }
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, currentUiData = null) }

            try {
                val response = sendMessageUseCase(_uiState.value.sessionId, text)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeEntity = NarrativeEntity.fromString(response.entity),
                        currentUiData = response.uiData
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong in the rain."
                    )
                }
            }
        }
    }

    fun selectChoice(choiceId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, currentUiData = null) }
            try {
                val response = sendMessageUseCase(_uiState.value.sessionId, "[CHOICE: $choiceId]")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeEntity = NarrativeEntity.fromString(response.entity),
                        currentUiData = response.uiData
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun castTriadVote(position: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, currentUiData = null) }
            try {
                val response = castTriadVoteUseCase(_uiState.value.sessionId, position)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeEntity = NarrativeEntity.fromString(response.entity),
                        currentUiData = response.uiData
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
