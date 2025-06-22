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

        val phrasesMap = mutableMapOf<String, String>()

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

        // ✅ Botão único para compartilhar todas as frases exibidas
        Button(
            onClick = {
                if (phrasesMap.isEmpty()) {
                    Toast.makeText(localContext, "Nenhuma frase para compartilhar", Toast.LENGTH_SHORT).show()
                } else {
                    val message = phrasesMap.entries.joinToString("\n") { (lang, phrase) ->
                        "$lang: $phrase"
                    }

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, message)
                    }

                    localContext.startActivity(Intent.createChooser(intent, "Compartilhar via"))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compartilhar Frases")
        }
    }
}