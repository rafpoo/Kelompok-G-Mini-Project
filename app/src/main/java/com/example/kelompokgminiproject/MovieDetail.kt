package com.example.kelompokgminiproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.kelompokgminiproject.api.ApiClient
import com.example.kelompokgminiproject.model.Movie   // 🔹 pakai model Movie yang sudah ada
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

class MovieDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_movie_detail)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })

        val backButton = findViewById<Button>(R.id.backButton)
        val posterView = findViewById<ImageView>(R.id.moviePoster)
        val movieTitle = findViewById<TextView>(R.id.movieTitle)
        val movieDetail1 = findViewById<TextView>(R.id.movieDetail1)
        val movieDetail2 = findViewById<TextView>(R.id.movieDetail2)
        val movieDetail3 = findViewById<TextView>(R.id.movieDetail3)
        val movieDesc = findViewById<TextView>(R.id.movieDescription)

        backButton.setOnClickListener { finish() }

        // Ambil judul movie dari Intent
        val movieTitleExtra = intent.getStringExtra("movie_title") ?: "Title not found"
        val encodedTitle = URLEncoder.encode(movieTitleExtra, StandardCharsets.UTF_8.toString())

        ApiClient.api.getMovie(encodedTitle, "b661cc7a", "full")
            .enqueue(object : retrofit2.Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: retrofit2.Response<Movie>) {
                    if (response.isSuccessful) {
                        val movie = response.body()
                        if (movie != null) {
                            runOnUiThread {
                                movieTitle.text = movie.Title ?: "No title"
                                movieDetail1.text = "Year: ${movie.Year ?: "-"}"
                                movieDetail2.text = "Genre: ${movie.Genre ?: "-"}"
                                movieDetail3.text = "IMDB Rating: ${movie.imdbRating ?: "-"} / 10"
                                movieDesc.text = movie.Plot ?: "Loading description ..."

                                Glide.with(this@MovieDetail)
                                    .load(movie.Poster)
                                    .placeholder(android.R.color.darker_gray)
                                    .into(posterView)
                            }
                        } else {
                            movieTitle.text = "Movie not found"
                        }
                    } else {
                        movieTitle.text = "Error: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    runOnUiThread { movieTitle.text = "Request failed: ${t.message}" }
                }
            })

    }
}
