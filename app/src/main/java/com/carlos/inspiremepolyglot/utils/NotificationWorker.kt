package com.carlos.inspiremepolyglot.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.carlos.inspiremepolyglot.R

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val phrases = JsonUtils.loadPhrases(applicationContext)
        val index = (0 until (phrases?.english?.size ?: 1)).random()

        val phrase = phrases?.english?.getOrNull(index) ?: "Your daily dose of inspiration!"
        showNotification("InspireMe Polyglot", phrase)

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "daily_phrase_channel"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Criação do canal de notificação (necessário no Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Frases Diárias",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
    }
}