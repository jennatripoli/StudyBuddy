package com.example.studybuddy.networkapi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JSON (
        @Json(name = "word") val word : String,
        @Json(name = "meanings") val meanings: List<Meanings>
        )

@JsonClass(generateAdapter = true)
data class Meanings (
    @Json(name = "partOfSpeech") val partOfSpeech : String,
    @Json(name = "definitions") val defs : List<Definitions>
        )

@JsonClass(generateAdapter = true)
data class Definitions (
    @Json(name = "definition") val def : String
)