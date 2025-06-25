package com.example.inspiremepolyglot.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
            `package` = "com.instagram.android"
        }

        context.grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Não foi possível abrir o Instagram", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}

private fun createInspiringImage(message: String): Bitmap {
    val width = 1080
    val height = 1920

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint()
    val gradient = LinearGradient(
        0f, 0f, 0f, height.toFloat(),
        Color.parseColor("#FF8A00"),
        Color.parseColor("#FF2E63"),
        Shader.TileMode.CLAMP
    )
    paint.shader = gradient
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

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