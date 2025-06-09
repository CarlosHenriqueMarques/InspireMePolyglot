package com.example.inspiremepolyglot.data

data class Phrase(
    val english: String,
    val portuguese: String,
    val french: String,
    val spanish: String
)

data class PhrasesResponse(
    val phrases: List<Phrase>
) 