package com.example.kelompokgminiproject.model

import com.squareup.moshi.Json

data class Movie(
    @field:Json(name = "Title") val title: String,
    @field:Json(name = "Year") val year: String,
    @field:Json(name = "Poster") val poster: String,
    @field:Json(name = "Genre") val genre: String
)
