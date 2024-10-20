package org.sniffsnirr.skillcinema.ui.movieman.filmography

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

class FilmographyAdapter(val onMovieClick: (Int,Int) -> Unit): RecyclerView.Adapter<FilmographyAdapter.MovieViewHolder>() {

    private var movieList = emptyList<MovieRVModel>().toMutableList()

    fun setData(movieList: List<MovieRVModel>) {
        this.movieList = movieList.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
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
        holder.binding.root.setOnClickListener {
            onMovieClick(movie.kinopoiskId?:0,position)
        }
    }

    fun updateMovieRVModel(position:Int){
        movieList[position].viewed=!movieList[position].viewed
        notifyItemChanged(position)
    }

    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}