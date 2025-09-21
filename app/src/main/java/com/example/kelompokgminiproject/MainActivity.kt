package com.example.kelompokgminiproject

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelompokgminiproject.api.MovieApiService
import com.example.kelompokgminiproject.model.MovieResponse
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var welcomeText: TextView
    private lateinit var movieList: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val movieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }

    private val apiKey = "b661cc7a"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        welcomeText = findViewById(R.id.welcome_text)
        movieList = findViewById(R.id.movie_list)

        movieList.layoutManager = LinearLayoutManager(this)

        // Listener untuk SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    fetchMovies(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Kembali tampilkan welcome text kalau kosong
                    welcomeText.visibility = View.VISIBLE
                    movieList.visibility = View.GONE
                }
                return true
            }
        })
    }

    private fun fetchMovies(query: String) {
        val call = movieApiService.searchMovies(apiKey, query)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.movies ?: emptyList()
                    if (movies.isNotEmpty()) {
                        welcomeText.visibility = View.GONE
                        movieList.visibility = View.VISIBLE
                        movieAdapter = MovieAdapter(movies)
                        movieList.adapter = movieAdapter
                    } else {
                        welcomeText.text = "Film tidak ditemukan"
                        welcomeText.visibility = View.VISIBLE
                        movieList.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Response error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
