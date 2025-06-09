package com.example.inspiremepolyglot.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhrasesRepository(private val context: Context) {
    private val gson = Gson()
    private var phrases: List<Phrase> = emptyList()

    suspend fun loadPhrases() {
        withContext(Dispatchers.IO) {
            val jsonString = context.assets.open("phrases.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<PhrasesResponse>() {}.type
            val response = gson.fromJson<PhrasesResponse>(jsonString, type)
            phrases = response.phrases
        }
    }

    fun getRandomPhrase(): Phrase? {
        return phrases.randomOrNull()
    }

    fun getPhraseForLanguage(phrase: Phrase, language: String): String {
        return when (language.lowercase()) {
            "english" -> phrase.english
            "portuguese" -> phrase.portuguese
            "french" -> phrase.french
            "spanish" -> phrase.spanish
            else -> phrase.english
        }
    }
} 