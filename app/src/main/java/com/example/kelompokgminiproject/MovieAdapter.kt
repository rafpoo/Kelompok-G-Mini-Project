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

class MovieAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.image_poster)
        val title: TextView = view.findViewById(R.id.text_title)
        val btnInfo: Button = view.findViewById(R.id.btn_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        Glide.with(holder.itemView.context)
            .load(movie.poster)
            .into(holder.poster)

        holder.btnInfo.setOnClickListener {
            // nanti bisa diarahkan ke DetailActivity
        }
    }

    override fun getItemCount(): Int = movies.size
}
