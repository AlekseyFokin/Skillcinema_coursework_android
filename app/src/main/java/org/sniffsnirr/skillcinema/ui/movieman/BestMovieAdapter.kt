package org.sniffsnirr.skillcinema.ui.movieman

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
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.databinding.ShowMeAllBinding
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

class BestMovieAdapter(

    val onCollectionClick: () -> Unit,
    val onMovieClick: (Int?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var movieList = emptyList<MovieRVModel>().toMutableList()

    fun setMovieList(movieList: List<MovieRVModel>) {
        this.movieList = movieList.toMutableList()
        notifyDataSetChanged()
    }

    inner class ButtonViewHolder(val binding: ShowMeAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.showMeAllBtn.setOnClickListener {
                onCollectionClick()
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
            binding.cd.setOnClickListener { onMovieClick(moviePoster.kinopoiskId) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (movieList[position].isButton) {
            MovieAdapter.BUTTON
        } else {
            MovieAdapter.MOVIE
        }
    }

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

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == MovieAdapter.BUTTON) {
            (holder as BestMovieAdapter.ButtonViewHolder).bind()
        } else {
            (holder as BestMovieAdapter.PosterViewHolder).bind(movieList[position])
        }
    }

}