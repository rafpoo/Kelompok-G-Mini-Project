package com.example.kelompokgminiproject

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelompokgminiproject.api.MovieApiService
import com.example.kelompokgminiproject.model.MovieResponse
import com.example.kelompokgminiproject.MovieAdapter
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var searchInput: EditText
    private lateinit var btnSearch: Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchInput = findViewById(R.id.search_input)
        btnSearch = findViewById(R.id.btn_search)
        welcomeText = findViewById(R.id.welcome_text)
        movieList = findViewById(R.id.movie_list)

        movieList.layoutManager = LinearLayoutManager(this)

        btnSearch.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotBlank()) {
                fetchMovies(query)
            }
        }

        val searchView = findViewById<SearchView>(R.id.searchView)

        // Default -> tampilakn welcome fragmentynya
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, WelcomeFragment())
            .commit()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val fragment = SearchResultFragment().apply {
                        arguments = Bundle().apply {
                            putString("query", query)
                        }
                    }

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, WelcomeFragment())
                        .commit()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, WelcomeFragment())
                        .commit()
                }

                return true
            }
        })
    }

    private val apiKey = "b661cc7a"
    private fun fetchMovies(query: String) {
        val call = movieApiService.searchMovies(apiKey, query)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.movies ?: emptyList()
                    welcomeText.visibility = View.GONE
                    movieList.visibility = View.VISIBLE
                    movieAdapter = MovieAdapter(movies)
                    movieList.adapter = movieAdapter
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
