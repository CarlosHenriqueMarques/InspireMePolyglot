package com.example.inspiremepolyglot.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun shareToWhatsApp(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        setPackage("com.whatsapp")
        putExtra(Intent.EXTRA_TEXT, message)
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp não está instalado", Toast.LENGTH_SHORT).show()
    }
}

fun shareToInstagramStory(context: Context, message: String) {
    try {
        // 1. Criar bitmap com o texto
        val bitmap = createBitmapFromText(message)

        // 2. Salvar bitmap em arquivo temporário
        val imageFile = File(context.cacheDir, "insta_story.png")
        val fos = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()

        // 3. Obter URI via FileProvider (configure o provider no manifest!)
        val imageUri: Uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            imageFile
        )

        // 4. Criar intent para Instagram Story
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(imageUri, "image/png")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage("com.instagram.android")
        }

        // 5. Enviar intent
        context.startActivity(intent)

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Instagram não está instalado ou erro ao compartilhar", Toast.LENGTH_SHORT).show()
    }
}

private fun createBitmapFromText(text: String): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 48f
        style = Paint.Style.FILL
    }

    val width = 1080
    val height = 1920
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.BLACK) // Fundo preto

    val x = 40f
    var y = 100f

    text.split("\n").forEach { line ->
        canvas.drawText(line, x, y, paint)
        y += paint.textSize * 1.5f
    }

    return bitmap
}