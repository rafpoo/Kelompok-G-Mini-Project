package com.example.kelompokgminiproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelompokgminiproject.model.Movie

class MovieAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.moviePoster)
        val title: TextView = view.findViewById(R.id.movieTitle)
        val subtitle: TextView = view.findViewById(R.id.movieSubtitle)
        val btnInfo: Button = view.findViewById(R.id.btnInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.title.text = movie.title
        holder.subtitle.text = movie.year // atau genre kalau ada

        Glide.with(holder.itemView.context)
            .load(movie.poster)
            .into(holder.poster)

        holder.btnInfo.setOnClickListener {
            // TODO: bisa arahkan ke detail activity
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateData(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
