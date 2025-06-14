package com.example.inspiremepolyglot.utils

import android.content.Context
import com.example.inspiremepolyglot.data.PhraseSet
import com.google.gson.Gson
import java.io.InputStreamReader

object JsonUtils {
    fun loadPhrases(context: Context): PhraseSet {
        val inputStream = context.assets.open("phrases.json")
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, PhraseSet::class.java)
    }
}