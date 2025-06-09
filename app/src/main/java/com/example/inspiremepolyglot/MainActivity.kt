package com.example.inspiremepolyglot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.inspiremepolyglot.ui.PhrasesScreen
import com.example.inspiremepolyglot.ui.PhrasesViewModel
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    private val viewModel: PhrasesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize AdMob
        MobileAds.initialize(this)

        setContent {
            MaterialTheme {
                Surface {
                    PhrasesScreen(viewModel)
                }
            }
        }
    }
} 