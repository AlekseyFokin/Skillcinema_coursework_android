package org.sniffsnirr.skillcinema.ui.collections.paging.compilations

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.sniffsnirr.skillcinema.R
import java.security.AccessController.getContext

// Адаптер с пагинацией для компиляций
class PagingCompilationAdapter(

    val onMovieClick: (Int?) -> Unit
) : PagingDataAdapter<MovieRVModel, PagingCompilationAdapter.MovieViewHolder>(DiffUtilCallback()) {
    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {

            if (!movie!!.viewed){
                Glide
                    .with(poster.context)
                    .load(movie?.imageUrl)
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .skipMemoryCache( true )
                    .into(poster)
                viewed.visibility = View.INVISIBLE
            }
            else{
                Glide.with(poster.context)
                    .asBitmap()
                    .load(movie?.imageUrl)
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .skipMemoryCache( true )
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            poster.background= BitmapDrawable(poster.context.resources,resource)
                            //setImageBitmap(resource)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
                poster.foreground=poster.context.getDrawable( R.drawable.gradient_viewed )
                viewed.visibility = View.VISIBLE
            }

            movieName.text = movie?.movieName
            genre.text = movie?.movieGenre
            if (movie?.rate?.trim() == "0" || movie?.rate?.trim() == "0.0") {
                raiting.visibility = View.INVISIBLE
            } else {
                raiting.text = movie?.rate
            }

        }
        holder.binding.root.setOnClickListener {
            movie?.let {
                onMovieClick(movie.kinopoiskId)
            }
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<MovieRVModel>() {
    override fun areItemsTheSame(oldItem: MovieRVModel, newItem: MovieRVModel): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: MovieRVModel, newItem: MovieRVModel): Boolean =
        oldItem == newItem
}