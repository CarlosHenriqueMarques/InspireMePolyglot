package com.example.inspiremepolyglot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


import com.example.inspiremepolyglot.ui.screen.PhrasesScreen
import com.example.inspiremepolyglot.ui.theme.InspireMePolyglotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InspireMePolyglotTheme {
                PhrasesScreen(context = this)
            }
        }
    }
}