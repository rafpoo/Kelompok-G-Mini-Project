package com.example.kelompokgminiproject.model

import com.squareup.moshi.Json

data class Movie(
    val Title: String?,
    val Year: String?,
    val Genre: String?,
    val imdbRating: String?,
    val Plot: String?,
    val Poster: String?
)

