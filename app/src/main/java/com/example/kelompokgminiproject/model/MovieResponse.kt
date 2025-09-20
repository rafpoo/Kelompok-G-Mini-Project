package com.example.kelompokgminiproject.model

import com.squareup.moshi.Json

data class MovieResponse(
    @field:Json(name = "Search") val movies: List<Movie>?
)
