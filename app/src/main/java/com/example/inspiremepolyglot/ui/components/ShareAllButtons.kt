package com.example.inspiremepolyglot.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun ShareAllButton(phrasesMap: Map<String, String>) {
    val context = LocalContext.current

    Button(onClick = {
        if (phrasesMap.isEmpty()) {
            Toast.makeText(context, "Nenhuma frase para compartilhar", Toast.LENGTH_SHORT).show()
            return@Button
        }

        val message = phrasesMap.entries.joinToString(separator = "\n") { (lang, phrase) ->
            "$lang: $phrase"
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }

        context.startActivity(Intent.createChooser(intent, "Compartilhar via"))
    }) {
        Text("Compartilhar Frases")
    }
}