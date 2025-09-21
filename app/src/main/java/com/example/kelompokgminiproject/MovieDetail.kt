package com.example.kelompokgminiproject

import android.os.Bundle
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
import java.util.concurrent.Executors

class MovieDetail : AppCompatActivity() {
    // Model untuk Moshi
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

        backButton.setOnClickListener(View.OnClickListener { v: View? -> finish() })

        // ExecutorService untuk thread background
        val executor = Executors.newSingleThreadExecutor()

        executor.execute(Runnable {
            try {
                // HTTP request
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://www.omdbapi.com/?apikey=YOUR_API_KEY&t=Avengers")
                    .build()
                val response = client.newCall(request).execute()
                val json = response.body()!!.string()

                // Parse JSON dengan Moshi
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter<Movie?>(Movie::class.java)
                val movie = adapter.fromJson(json)

                // Update UI di main thread
                runOnUiThread(Runnable {
                    if (movie != null) {
                        movieTitle.setText(if (movie.title != null) movie.title else "No title")
                        movieDetail1.setText("Year: " + (if (movie.year != null) movie.year else "-"))
                        movieDetail2.setText("Genre: " + (if (movie.genre != null) movie.genre else "-"))

                        Glide.with(this@MovieDetail)
                            .load(movie.poster)
                            .placeholder(android.R.color.darker_gray)
                            .into(posterView)
                    } else {
                        movieTitle.setText("Parsing error")
                    }
                })
            } catch (e: IOException) {
                runOnUiThread(Runnable { movieTitle.setText("Error: " + e.message) })
            }
        })
    }
}
