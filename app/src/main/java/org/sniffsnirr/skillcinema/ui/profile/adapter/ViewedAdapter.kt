package org.sniffsnirr.skillcinema.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.databinding.ShowMeAllBinding
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.ButtonViewHolder
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.Companion
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.PosterViewHolder
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

class ViewedAdapter( var movieModel: List<MovieRVModel>,
                     val onCollectionClick: (String) -> Unit,
                     val onMovieClick: (Int?) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MovieAdapter.BUTTON) {
            val binding =
                ShowMeAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ButtonViewHolder(binding)
        } else {
            val binding =
                MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PosterViewHolder(binding)
        }
    }

    override fun getItemCount()=movieModel.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MovieAdapter.BUTTON) {
            (holder as org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.ButtonViewHolder).bind(movieModel[position])
        } else {
            (holder as org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.PosterViewHolder).bind(movieModel[position])
        }
    }


    inner class ButtonViewHolder(val binding: ShowMeAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieModel: MovieRVModel) {
            binding.showMeAllBtn.setOnClickListener {
                onCollectionClick(movieModel.categoryDescription?.first ?: "")
            }
        }
    }

    inner class PosterViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(moviePoster: MovieRVModel) {
            binding.apply {
                Glide
                    .with(poster.context)
                    .load(moviePoster.imageUrl)
                    .into(poster)
                movieName.text = moviePoster.movieName
                genre.text = moviePoster.movieGenre
                if (moviePoster.rate.trim() == "0" || moviePoster.rate.trim() == "0.0") {
                    raiting.visibility = View.INVISIBLE
                } else {
                    raiting.text = moviePoster.rate
                }
                if (moviePoster.viewed) {
                    viewed.visibility = View.VISIBLE
                } else {
                    viewed.visibility = View.INVISIBLE
                }
            }
            binding.cd.setOnClickListener { onMovieClick(moviePoster.kinopoiskId) }
        }
    }
    companion object {
        const val MOVIE = 0
        const val BUTTON = 1
    }
}