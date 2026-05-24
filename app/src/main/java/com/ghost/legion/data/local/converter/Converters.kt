package com.ghost.legion.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.ghost.legion.domain.model.Faction
import com.ghost.legion.domain.model.CausalEvent

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = try {
        json.decodeFromString(value)
    } catch (e: Exception) {
        emptyList()
    }

    @TypeConverter
    fun fromFactionList(value: List<Faction>): String = json.encodeToString(value)

    @TypeConverter
    fun toFactionList(value: String): List<Faction> = try {
        json.decodeFromString(value)
    } catch (e: Exception) {
        emptyList()
    }

    @TypeConverter
    fun fromCausalEventList(value: List<CausalEvent>): String = json.encodeToString(value)

    @TypeConverter
    fun toCausalEventList(value: String): List<CausalEvent> = try {
        json.decodeFromString(value)
    } catch (e: Exception) {
        emptyList()
    }
}
