package com.example.inspiremepolyglot.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inspiremepolyglot.data.Phrase
import com.example.inspiremepolyglot.data.PhrasesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhrasesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PhrasesRepository(application)
    
    private val _currentPhrase = MutableStateFlow<Phrase?>(null)
    val currentPhrase: StateFlow<Phrase?> = _currentPhrase.asStateFlow()

    private val _selectedLanguages = MutableStateFlow(setOf("english"))
    val selectedLanguages: StateFlow<Set<String>> = _selectedLanguages.asStateFlow()

    init {
        loadPhrases()
    }

    private fun loadPhrases() {
        viewModelScope.launch {
            repository.loadPhrases()
            generateNewPhrase()
        }
    }

    fun generateNewPhrase() {
        _currentPhrase.value = repository.getRandomPhrase()
    }

    fun toggleLanguage(language: String) {
        val current = _selectedLanguages.value.toMutableSet()
        if (current.contains(language)) {
            if (current.size > 1) {
                current.remove(language)
            }
        } else {
            current.add(language)
        }
        _selectedLanguages.value = current
    }

    fun getPhraseForLanguage(language: String): String {
        return currentPhrase.value?.let { repository.getPhraseForLanguage(it, language) } ?: ""
    }
} 