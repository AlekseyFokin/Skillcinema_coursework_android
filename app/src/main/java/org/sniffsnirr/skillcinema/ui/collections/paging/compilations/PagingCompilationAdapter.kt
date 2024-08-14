package org.sniffsnirr.skillcinema.ui.collections.paging.compilations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class PagingCompilationAdapter(

    val onMovieClick: (String) -> Unit
) : PagingDataAdapter<MovieRVModel, PagingCompilationAdapter.MovieViewHolder>(DiffUtilCallback()) {
    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    //  override fun getItemCount() = movieModel.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)//movieModel[position]  val item
        with(holder.binding) {
            Glide
                .with(poster.context)
                .load(movie?.imageUrl)//.placeholder(R.drawable.baseline_auto_awesome_24)
                .into(poster)
            movieName.text = movie?.movieName
            genre.text = movie?.movieGenre
            if (movie?.rate?.trim() == "0" || movie?.rate?.trim() == "0.0") {
                raiting.visibility = View.INVISIBLE
            } else {
                raiting.text = movie?.rate
            }
            if (movie?.viewed == true) {
                viewed.visibility = View.VISIBLE
            } else {
                viewed.visibility = View.INVISIBLE
            }
        }
        holder.binding.root.setOnClickListener {
            movie?.let {
                onMovieClick(movie.movieName)
            }
        }
    }
}
class DiffUtilCallback : DiffUtil.ItemCallback<MovieRVModel>() {
    override fun areItemsTheSame(oldItem: MovieRVModel, newItem: MovieRVModel): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: MovieRVModel, newItem: MovieRVModel): Boolean = oldItem == newItem
}