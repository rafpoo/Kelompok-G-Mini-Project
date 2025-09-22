package com.example.kelompokgminiproject.api


import com.example.kelompokgminiproject.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("/") // baseUrl nanti di MainActivity
    fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String
    ): Call<MovieResponse>
}
