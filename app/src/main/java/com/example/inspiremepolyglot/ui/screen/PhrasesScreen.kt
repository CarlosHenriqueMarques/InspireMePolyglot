package com.example.inspiremepolyglot.ui.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import  com.example.inspiremepolyglot.utils.JsonUtils
import com.example.inspiremepolyglot.utils.shareToInstagramStory
import com.example.inspiremepolyglot.utils.shareToWhatsApp

@Composable
fun PhrasesScreen(context: Context) {
    val phraseList = remember { JsonUtils.loadPhrases(context) }
    val languages = listOf("English", "Portuguese", "French", "Spanish")

    // ✅ Todos selecionados por padrão
    val selectedLanguages = remember { mutableStateListOf(*languages.toTypedArray()) }

    // ✅ Índice da frase atual
    var currentPhraseIndex by remember { mutableStateOf(0) }

    // ✅ Limite de frases (mínimo entre os idiomas)
    val maxIndex = remember(phraseList) {
        phraseList?.let {
            minOf(
                it.english.size,
                it.portuguese.size,
                it.french.size,
                it.spanish.size
            ) - 1
        } ?: 0
    }

    // ✅ Gera nova frase
    fun generateNewPhraseIndex() {
        currentPhraseIndex = (0..maxIndex).random()
    }

    // ✅ Primeira frase ao iniciar
    LaunchedEffect(Unit) {
        generateNewPhraseIndex()
    }

    val localContext = LocalContext.current
    val phrasesMap = remember { mutableStateMapOf<String, String>() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Selecione os idiomas:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        languages.forEach { lang ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedLanguages.contains(lang),
                    onCheckedChange = { isChecked ->
                        if (isChecked) selectedLanguages.add(lang)
                        else selectedLanguages.remove(lang)
                    }
                )
                Text(text = lang)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { generateNewPhraseIndex() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nova Frase")
        }

        Spacer(modifier = Modifier.height(16.dp))

        phrasesMap.clear()

        phraseList?.let {
            selectedLanguages.forEach { lang ->
                val phrase = when (lang) {
                    "English" -> it.english.getOrNull(currentPhraseIndex)
                    "Portuguese" -> it.portuguese.getOrNull(currentPhraseIndex)
                    "French" -> it.french.getOrNull(currentPhraseIndex)
                    "Spanish" -> it.spanish.getOrNull(currentPhraseIndex)
                    else -> null
                }

                phrase?.let { safePhrase ->
                    phrasesMap[lang] = safePhrase
                    Text(
                        text = "$lang: $safePhrase",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔁 Texto plano para WhatsApp
        val shareMessageWhatsApp = phrasesMap.entries.joinToString("\n") { (lang, phrase) ->
            "$lang: $phrase"
        }

        // 🖼️ Mensagem formatada para Instagram Story
        val emojiMap = mapOf(
            "English" to "\uD83C\uDDFA\uD83C\uDDF8",
            "Portuguese" to "\uD83C\uDDE7\uD83C\uDDF7",
            "French" to "\uD83C\uDDEB\uD83C\uDDF7",
            "Spanish" to "\uD83C\uDDEA\uD83C\uDDF8"
        )

        val shareMessageInstagram = phrasesMap.entries.joinToString("\n\n") { (lang, phrase) ->
            "${emojiMap[lang] ?: ""} $lang\n$phrase"
        }

        // ✅ Botão: Compartilhar no WhatsApp
        Button(
            onClick = {
                if (phrasesMap.isEmpty()) {
                    Toast.makeText(localContext, "Nenhuma frase para compartilhar", Toast.LENGTH_SHORT).show()
                } else {
                    shareToWhatsApp(localContext, shareMessageWhatsApp)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compartilhar no WhatsApp")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Botão: Compartilhar no Instagram Story
        Button(
            onClick = {
                if (phrasesMap.isEmpty()) {
                    Toast.makeText(localContext, "Nenhuma frase para compartilhar", Toast.LENGTH_SHORT).show()
                } else {
                    shareToInstagramStory(localContext, shareMessageInstagram)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compartilhar no Instagram Story")
        }
    }
}