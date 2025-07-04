package com.carlos.inspiremepolyglot

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.carlos.inspiremepolyglot.ui.screen.PhrasesScreen
import com.carlos.inspiremepolyglot.ui.theme.InspireMePolyglotTheme
import com.carlos.inspiremepolyglot.utils.NotificationWorker
import com.google.android.gms.ads.MobileAds
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InspireMePolyglotTheme {
                PhrasesScreen(context = this)
            }
        }
        MobileAds.initialize(this) {}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            } else {
                scheduleDailyNotification()
            }
        } else {
            scheduleDailyNotification()
        }
    }

    private fun scheduleDailyNotification() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_notification",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}