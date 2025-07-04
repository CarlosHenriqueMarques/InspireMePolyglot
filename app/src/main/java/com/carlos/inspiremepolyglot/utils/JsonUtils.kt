package com.carlos.inspiremepolyglot.utils

import android.content.Context
import com.carlos.inspiremepolyglot.data.model.PhraseList
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object JsonUtils {

suspend fun loadPhrases(context: Context): PhraseList {
    return withContext(Dispatchers.IO) {
        val input = context.assets.open("phrases.json")
        val json = input.bufferedReader().use { it.readText() }
        Gson().fromJson(json, PhraseList::class.java)
    }
}
}