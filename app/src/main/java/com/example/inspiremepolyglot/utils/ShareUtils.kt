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
import android.graphics.*



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
        val bitmap = createInspiringImage(message)

        val file = File(context.cacheDir, "story_image.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(uri, "image/png")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //putExtra("interactive_asset_uri", uri)
            `package` = "com.instagram.android"
        }

        context.grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Não foi possível abrir o Instagram", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}

private fun createBitmapFromText(text: String): Bitmap {
    val width = 1080
    val height = 1920

    // Criar o bitmap e canvas
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Fundo com gradiente
    val backgroundPaint = Paint()
    val gradient = LinearGradient(
        0f, 0f, 0f, height.toFloat(),
        Color.parseColor("#FF8A00"),  // Laranja
        Color.parseColor("#FF2E63"),  // Rosa
        Shader.TileMode.CLAMP
    )
    backgroundPaint.shader = gradient
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

    // Estilo do texto
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 54f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    val x = width / 2f
    var y = 150f

    // Renderiza cada linha, com espaço extra entre blocos de idioma
    val lines = text.split("\n")
    for (line in lines) {
        if (line.isBlank()) {
            y += paint.textSize * 1.5f  // espaço entre blocos
        } else {
            canvas.drawText(line, x, y, paint)
            y += paint.textSize * 1.2f
        }
    }

    return bitmap
}

private fun createInspiringImage(message: String): Bitmap {
    val width = 1080
    val height = 1920

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Gradiente de fundo
    val paint = Paint()
    val gradient = LinearGradient(
        0f, 0f, 0f, height.toFloat(),
        Color.parseColor("#FF8A00"),
        Color.parseColor("#FF2E63"),
        Shader.TileMode.CLAMP
    )
    paint.shader = gradient
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

    // Texto centralizado, com quebras de linha e espaçamento
    val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 56f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    val lines = message.split("\n")
    val lineHeight = textPaint.textSize * 1.8f
    val totalHeight = lines.size * lineHeight
    var y = (height - totalHeight) / 2f

    for (line in lines) {
        canvas.drawText(line, width / 2f, y, textPaint)
        y += lineHeight
    }

    return bitmap
}