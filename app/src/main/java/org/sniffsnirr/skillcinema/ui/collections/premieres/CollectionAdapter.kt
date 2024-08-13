package org.sniffsnirr.skillcinema.ui.collections.premieres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

class CollectionAdapter(
    var movieModel: List<MovieRVModel>,
    val onMovieClick: (String) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount()=movieModel.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieModel[position]
        with(holder.binding) {
            Glide
                .with(poster.context)
                .load(movie.imageUrl)//.placeholder(R.drawable.baseline_auto_awesome_24)
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
        holder.binding.root.setOnClickListener {
            onMovieClick(movie.movieName)
        }

    }
}