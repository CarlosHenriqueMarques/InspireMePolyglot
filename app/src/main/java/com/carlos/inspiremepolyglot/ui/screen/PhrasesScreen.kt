package com.carlos.inspiremepolyglot.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.carlos.inspiremepolyglot.utils.JsonUtils
import com.carlos.inspiremepolyglot.utils.shareToInstagramStory
import com.carlos.inspiremepolyglot.utils.shareToWhatsApp
import com.carlos.inspiremepolyglot.ui.components.AdBanner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import com.carlos.inspiremepolyglot.data.model.PhraseList
import kotlinx.coroutines.delay

@Composable
fun PhrasesScreen(context: Context) {
    //val phraseList = remember { JsonUtils.loadPhrases(context) }
    var phraseList by remember { mutableStateOf<PhraseList?>(null) }
    val languages = listOf("English", "Portuguese", "French", "Spanish")
    val selectedLanguages = remember { mutableStateListOf(*languages.toTypedArray()) }
    var currentPhraseIndex by remember { mutableStateOf(0) }



    // Carrega frases de forma assíncrona (não trava UI)
    LaunchedEffect(Unit) {
        phraseList = JsonUtils.loadPhrases(context)
    }


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

//    LaunchedEffect(Unit) {
//        currentPhraseIndex = (0..maxIndex).random()
//    }

    LaunchedEffect(phraseList) {
        phraseList?.let {
            val max = minOf(it.english.size, it.portuguese.size, it.french.size, it.spanish.size) - 1
            currentPhraseIndex = (0..max).random()
        }
    }

    val localContext = LocalContext.current
    val phrasesMap = remember { mutableStateMapOf<String, String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // ✅ adapta ao tema
            .padding(24.dp)
    ) {
        Text(
            text = "Selecione os idiomas:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground // ✅ visível nos dois temas
        )

        Spacer(modifier = Modifier.height(12.dp))

        languages.forEach { lang ->
            AnimatedVisibility(visible = true) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = selectedLanguages.contains(lang),
                        onCheckedChange = { isChecked ->
                            if (isChecked) selectedLanguages.add(lang)
                            else selectedLanguages.remove(lang)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = lang,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { currentPhraseIndex = (0..maxIndex).random() },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Nova Frase", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                    Crossfade(targetState = "$lang: $safePhrase") { animatedText ->
                        Text(
                            text = animatedText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val shareMessageWhatsApp = phrasesMap.entries.joinToString("\n") { (lang, phrase) ->
            "$lang: $phrase"
        }

        val emojiMap = mapOf(
            "English" to "\uD83C\uDDFA\uD83C\uDDF8",
            "Portuguese" to "\uD83C\uDDE7\uD83C\uDDF7",
            "French" to "\uD83C\uDDEB\uD83C\uDDF7",
            "Spanish" to "\uD83C\uDDEA\uD83C\uDDF8"
        )

        val shareMessageInstagram = phrasesMap.entries.joinToString("\n\n") { (lang, phrase) ->
            "${emojiMap[lang] ?: ""} $lang\n$phrase"
        }

        Button(
            onClick = {
                if (phrasesMap.isEmpty()) {
                    Toast.makeText(localContext, "Nenhuma frase para compartilhar", Toast.LENGTH_SHORT).show()
                } else {
                    shareToWhatsApp(localContext, shareMessageWhatsApp)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Compartilhar no WhatsApp")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (phrasesMap.isEmpty()) {
                    Toast.makeText(localContext, "Nenhuma frase para compartilhar", Toast.LENGTH_SHORT).show()
                } else {
                    shareToInstagramStory(localContext, shareMessageInstagram)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Compartilhar no Instagram Story")
        }

        Spacer(modifier = Modifier.height(16.dp))

        var showAd by remember { mutableStateOf(false) }
        LaunchedEffect(phraseList) {
            if (phraseList != null) {
                delay(500)
                showAd = true
            }
        }

        if (showAd) {
            AdBanner(context = context, modifier = Modifier.fillMaxWidth())
        }
    }
}
