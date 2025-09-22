package com.example.kelompokgminiproject

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelompokgminiproject.adapter.MovieAdapter
import com.example.kelompokgminiproject.api.MovieApiService
import com.example.kelompokgminiproject.model.Movie
import com.example.kelompokgminiproject.model.MovieResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var welcomeText: TextView
    private lateinit var movieList: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var fabNext: FloatingActionButton
    private lateinit var fabBack: FloatingActionButton


    private var allMovies: List<Movie> = emptyList()
    private var currentPage = 0
    private val pageSize = 4

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

        searchEditText = findViewById(R.id.searchEditText)
        welcomeText = findViewById(R.id.welcome_text)
        movieList = findViewById(R.id.movie_list)
        fabNext = findViewById(R.id.fabNext)
        fabBack = findViewById(R.id.fabBack)   // 🔹 inisialisasi tombol Back

        movieList.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(emptyList())
        movieList.adapter = movieAdapter

        // Search action: tekan enter untuk submit
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                fetchMovies(query)
            }
            true
        }

        // Tombol Next untuk pagination
        fabNext.setOnClickListener {
            if (allMovies.isNotEmpty()) {
                val maxPage = (allMovies.size + pageSize - 1) / pageSize
                if (currentPage < maxPage - 1) {
                    currentPage++
                    showPage()
                } else {
                    Toast.makeText(this, "Sudah halaman terakhir", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 🔹 Tombol Back untuk pagination
        fabBack.setOnClickListener {
            if (allMovies.isNotEmpty()) {
                if (currentPage > 0) {
                    currentPage--
                    showPage()
                } else {
                    Toast.makeText(this, "Sudah di halaman pertama", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchMovies(query: String) {
        val call = movieApiService.searchMovies(apiKey, query)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    allMovies = response.body()?.movies ?: emptyList()
                    currentPage = 0
                    if (allMovies.isNotEmpty()) {
                        welcomeText.visibility = View.GONE
                        movieList.visibility = View.VISIBLE
                        showPage()
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

    private fun showPage() {
        val fromIndex = currentPage * pageSize
        val toIndex = minOf(fromIndex + pageSize, allMovies.size)
        val pageMovies = allMovies.subList(fromIndex, toIndex)
        movieAdapter.updateData(pageMovies)
    }
}
