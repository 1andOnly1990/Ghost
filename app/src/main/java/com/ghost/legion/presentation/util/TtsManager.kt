package com.ghost.legion.presentation.util

import android.content.Context
import android.speech.tts.TextToSpeech
import com.ghost.legion.domain.model.NarrativeEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isInitialized = true
        }
    }

    fun speak(text: String, entity: NarrativeEntity) {
        if (!isInitialized) return
        
        when (entity) {
            NarrativeEntity.AURA -> {
                tts?.setPitch(1.2f)
                tts?.setSpeechRate(1.1f)
            }
            NarrativeEntity.ECHO -> {
                tts?.setPitch(0.8f)
                tts?.setSpeechRate(0.8f)
            }
            NarrativeEntity.DEVON -> {
                tts?.setPitch(0.9f)
                tts?.setSpeechRate(1.0f)
            }
            NarrativeEntity.LEGION -> {
                tts?.setPitch(0.7f)
                tts?.setSpeechRate(0.9f)
            }
            NarrativeEntity.SYSTEM -> {
                tts?.setPitch(1.0f)
                tts?.setSpeechRate(1.0f)
            }
        }
        
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance_${System.currentTimeMillis()}")
    }
    
    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
