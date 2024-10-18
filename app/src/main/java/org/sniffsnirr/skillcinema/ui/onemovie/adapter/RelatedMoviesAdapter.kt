package org.sniffsnirr.skillcinema.ui.onemovie.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

// адаптер для rv содрежащего подобные фильмы

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
            if (!movie!!.viewed){
                Glide
                    .with(poster.context)
                    .load(movie?.imageUrl)
                    .into(poster)
                viewed.visibility = View.INVISIBLE
            }
            else{
                Glide.with(poster.context)
                    .asBitmap()
                    .load(movie?.imageUrl)
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            poster.background= BitmapDrawable(poster.context.resources,resource)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
                poster.foreground=poster.context.getDrawable( R.drawable.gradient_viewed )
                viewed.visibility = View.VISIBLE
            }

            movieName.text = movie.movieName
            genre.text = movie.movieGenre
            if (movie.rate.trim() == "0" || movie.rate.trim() == "0.0") {
                raiting.visibility = View.INVISIBLE
            } else {
                raiting.text = movie.rate
            }
        }
        holder.binding.cd.setOnClickListener { onMovieClick(movie.kinopoiskId) }
    }

    inner class RelatedMoviesHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}