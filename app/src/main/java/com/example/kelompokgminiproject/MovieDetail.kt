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
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

class MovieDetail : AppCompatActivity() {
    // Model untuk parsing detail movie
    class Movie {
        @Json(name = "Title")
        var title: String? = null

        @Json(name = "Year")
        var year: String? = null

        @Json(name = "Genre")
        var genre: String? = null

        @Json(name = "Poster")
        var poster: String? = null
    }

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

        backButton.setOnClickListener { finish() }

        // Ambil judul movie dari Intent
        val movieTitleExtra = intent.getStringExtra("movie_title") ?: "Avengers"
        val encodedTitle = URLEncoder.encode(movieTitleExtra, StandardCharsets.UTF_8.toString())

        // ExecutorService untuk background thread
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {
                // HTTP request pakai judul dari Intent
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://www.omdbapi.com/?apikey=b661cc7a&t=$encodedTitle")
                    .build()
                val response = client.newCall(request).execute()
                val json = response.body()!!.string()

                // Log JSON biar bisa cek di Logcat
                Log.d("MovieDetail", "Response JSON: $json")

                // Parse JSON dengan Moshi
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(Movie::class.java)
                val movie = adapter.fromJson(json)

                // Update UI di main thread
                runOnUiThread {
                    if (movie != null) {
                        movieTitle.text = movie.title ?: "No title"
                        movieDetail1.text = "Year: " + (movie.year ?: "-")
                        movieDetail2.text = "Genre: " + (movie.genre ?: "-")

                        Glide.with(this@MovieDetail)
                            .load(movie.poster)
                            .placeholder(android.R.color.darker_gray)
                            .into(posterView)
                    } else {
                        movieTitle.text = "Parsing error"
                    }
                }
            } catch (e: IOException) {
                runOnUiThread { movieTitle.text = "Error: ${e.message}" }
            }
        }
    }
}
