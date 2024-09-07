package org.sniffsnirr.skillcinema.ui.onemovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

// адаптер для rv содрежащего подобные фильму

class RelatedMoviesAdapter(val onMovieClick: (Int?) -> Unit) : RecyclerView.Adapter<RelatedMoviesAdapter.RelatedMoviesHolder>() {

    private var relatedMovies: List<MovieRVModel> = emptyList()

    fun setData(relatedMovies: List<MovieRVModel>) {
        this.relatedMovies = relatedMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedMoviesHolder {
        return RelatedMoviesHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = relatedMovies.size

    override fun onBindViewHolder(holder: RelatedMoviesHolder, position: Int) {
        val movie = relatedMovies[position]
        with(holder.binding) {
            Glide
                .with(poster.context)
                .load(movie.imageUrl)
                .into(poster)
            movieName.text = movie.movieName
            genre.text = movie.movieGenre
            if (movie.rate.trim() == "0" || movie.rate.trim() == "0.0") {
                raiting.visibility = View.INVISIBLE
            } else {
                raiting.text = movie.rate
            }
            if (movie.viewed) {
                viewed.visibility = View.VISIBLE
            } else {
                viewed.visibility = View.INVISIBLE
            }
        }
        holder.binding.cd.setOnClickListener { onMovieClick(movie.kinopoiskId) }
    }

    inner class RelatedMoviesHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}