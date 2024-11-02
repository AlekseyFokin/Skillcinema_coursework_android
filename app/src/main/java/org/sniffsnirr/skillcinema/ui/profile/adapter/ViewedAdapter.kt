package org.sniffsnirr.skillcinema.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.sniffsnirr.skillcinema.databinding.ClearHistoryBinding
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

// Адаптер для RV для коллекции просмотренных фильмов
class ViewedAdapter(
            val onButtonClearAllClick: () -> Unit,
            val onMovieClick: (Int?,Int) -> Unit
    )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var movies = emptyList<MovieRVModel>().toMutableList()
    fun setData(movies: List<MovieRVModel>?) {
        this.movies = movies?.toMutableList()?:emptyList<MovieRVModel>().toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == BUTTON) {
            val binding =
                ClearHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ButtonViewHolder(binding)
        } else {
            val binding =
                MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PosterViewHolder(binding)
        }
    }

    override fun getItemCount()=movies.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!movies.isNullOrEmpty()){
        if (getItemViewType(position) == BUTTON) {
            (holder as ButtonViewHolder).bind()
        } else {
            (holder as PosterViewHolder).bind(movies[position])
        }
    }
    }

    fun deleteFromRV(position:Int){
        movies.drop(position)
        notifyItemRemoved(position)
    }


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
                Glide
                    .with(poster.context)
                    .load(moviePoster.imageUrl)
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .skipMemoryCache( true )
                    .into(poster)
                movieName.text = moviePoster.movieName
                genre.text = moviePoster.movieGenre
                if (moviePoster.rate.trim() == "0" || moviePoster.rate.trim() == "0.0") {
                    raiting.visibility = View.INVISIBLE
                } else {
                    raiting.text = moviePoster.rate
                }
//                if (moviePoster.viewed) {
//                    viewed.visibility = View.VISIBLE
//                } else {
//                    viewed.visibility = View.INVISIBLE
//                }
            }
            binding.cd.setOnClickListener { onMovieClick(moviePoster.kinopoiskId, getBindingAdapterPosition()) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (movies[position].isButton) {
            BUTTON
        } else {
            MOVIE
        }
    }
    companion object {
        const val MOVIE = 0
        const val BUTTON = 1
    }
}