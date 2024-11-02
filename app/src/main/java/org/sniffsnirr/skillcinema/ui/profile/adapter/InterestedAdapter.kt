package org.sniffsnirr.skillcinema.ui.profile.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.ClearHistoryBinding
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel


//Адаптер для RV для коллекции фильмов, которые просматривал пользователь
class InterestedAdapter(
    val onButtonClearAllClick: () -> Unit,
    val onMovieClick: (Int?, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var movies = emptyList<MovieRVModel>().toMutableList()
    fun setData(movies: List<MovieRVModel>?) {
        this.movies = movies?.toMutableList() ?: emptyList<MovieRVModel>().toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ViewedAdapter.BUTTON) {
            val binding =
                ClearHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ButtonViewHolder(binding)
        } else {
            val binding =
                MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PosterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!movies.isNullOrEmpty()) {
            if (getItemViewType(position) == ViewedAdapter.BUTTON) {
                (holder as InterestedAdapter.ButtonViewHolder).bind()
            } else {
                (holder as InterestedAdapter.PosterViewHolder).bind(movies[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (movies[position].isButton) {
            BUTTON
        } else {
            MOVIE
        }
    }

    override fun getItemCount() = movies.size

    inner class ButtonViewHolder(val binding: ClearHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.showMeAllBtn.setOnClickListener {
                onButtonClearAllClick()
            }
        }
    }

    inner class PosterViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(moviePoster: MovieRVModel) {
            binding.apply {
                if (!moviePoster.viewed) {
                    Glide
                        .with(poster.context)
                        .load(moviePoster.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(poster)
                    viewed.visibility = View.INVISIBLE
                } else {
                    Glide.with(poster.context)
                        .asBitmap()
                        .load(moviePoster.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                poster.background =
                                    BitmapDrawable(poster.context.resources, resource)
                                //setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    poster.foreground = poster.context.getDrawable(R.drawable.gradient_viewed)
                    viewed.visibility = View.VISIBLE
                }

                movieName.text = moviePoster.movieName
                genre.text = moviePoster.movieGenre
                if (moviePoster.rate.trim() == "0" || moviePoster.rate.trim() == "0.0") {
                    raiting.visibility = View.INVISIBLE
                } else {
                    raiting.text = moviePoster.rate
                }
            }
            binding.cd.setOnClickListener {
                onMovieClick(
                    moviePoster.kinopoiskId,
                    getBindingAdapterPosition()
                )
            }
        }
    }

    companion object {
        const val MOVIE = 0
        const val BUTTON = 1
    }
}