package com.example.inspiremepolyglot.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inspiremepolyglot.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import android.content.Intent
import android.net.Uri

@Composable
fun PhrasesScreen(viewModel: PhrasesViewModel) {
    val currentPhrase by viewModel.currentPhrase.collectAsState()
    val selectedLanguages by viewModel.selectedLanguages.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LanguageSelector(
            selectedLanguages = selectedLanguages,
            onLanguageSelected = { viewModel.toggleLanguage(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(selectedLanguages.toList()) { language ->
                PhraseCard(
                    phrase = viewModel.getPhraseForLanguage(language),
                    language = language,
                    onShare = { phrase ->
                        sharePhrase(context, phrase)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.generateNewPhrase() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.generate_new_phrase))
        }

        Spacer(modifier = Modifier.height(16.dp))

        AdBanner()
    }
}

@Composable
fun LanguageSelector(
    selectedLanguages: Set<String>,
    onLanguageSelected: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.select_languages),
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LanguageChip("english", selectedLanguages, onLanguageSelected)
            LanguageChip("portuguese", selectedLanguages, onLanguageSelected)
            LanguageChip("french", selectedLanguages, onLanguageSelected)
            LanguageChip("spanish", selectedLanguages, onLanguageSelected)
        }
    }
}

@Composable
fun LanguageChip(
    language: String,
    selectedLanguages: Set<String>,
    onLanguageSelected: (String) -> Unit
) {
    val isSelected = selectedLanguages.contains(language)
    FilterChip(
        selected = isSelected,
        onClick = { onLanguageSelected(language) },
        label = { Text(language.capitalize()) }
    )
}

@Composable
fun PhraseCard(
    phrase: String,
    language: String,
    onShare: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = language.capitalize(),
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = phrase,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(
                onClick = { onShare(phrase) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Share, contentDescription = stringResource(R.string.share))
            }
        }
    }
}

@Composable
fun AdBanner() {
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-4822152027214407~1574920010" // Test ad unit ID
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

private fun sharePhrase(context: Context, phrase: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, phrase)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
} 