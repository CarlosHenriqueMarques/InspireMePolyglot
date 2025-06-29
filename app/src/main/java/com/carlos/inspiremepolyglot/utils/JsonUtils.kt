package com.carlos.inspiremepolyglot.utils

import android.content.Context
import com.carlos.inspiremepolyglot.data.model.PhraseList
import com.google.gson.Gson
import java.io.InputStreamReader

object JsonUtils {
    fun loadPhrases(context: Context): PhraseList {
        val inputStream = context.assets.open("phrases.json")
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, PhraseList::class.java)
    }
}