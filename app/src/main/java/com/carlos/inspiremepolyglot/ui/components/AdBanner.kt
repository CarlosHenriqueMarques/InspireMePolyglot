package com.carlos.inspiremepolyglot.ui.components

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*

@Composable
fun AdBanner(context: Context, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        MobileAds.initialize(context)
    }

    AndroidView(
        factory = { ctx ->
            val adView = AdView(ctx)
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = "ca-app-pub-4822152027214407/5018421139"
            adView.loadAd(AdRequest.Builder().build())
            adView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            adView
        },
        modifier = modifier
    )
}
