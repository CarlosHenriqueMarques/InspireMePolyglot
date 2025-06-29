package com.carlos.inspiremepolyglot.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ShareButtons(sharedText: String) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                shareToWhatsApp(context, sharedText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Compartilhar no WhatsApp")
        }

        Button(
            onClick = {
                shareToInstagramStory(context, sharedText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Compartilhar no Instagram Story")
        }
    }
}

fun shareToWhatsApp(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.setPackage("com.whatsapp")
    intent.putExtra(Intent.EXTRA_TEXT, message)

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp não está instalado", Toast.LENGTH_SHORT).show()
    }
}

fun shareToInstagramStory(context: Context, message: String) {
    val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
        `package` = "com.instagram.android"
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Instagram não está instalado", Toast.LENGTH_SHORT).show()
    }
}