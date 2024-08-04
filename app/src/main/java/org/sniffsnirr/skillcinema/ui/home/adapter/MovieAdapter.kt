package org.sniffsnirr.skillcinema.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

class MovieAdapter(var movieModel: List<MovieRVModel>):RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)
        return MovieViewHolder(view)
    }

    override fun getItemCount()=movieModel.size


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        holder.binding.apply {
            Glide
                .with(poster.context)
                .load(movieModel[position].imageUrl)//.placeholder(R.drawable.baseline_auto_awesome_24)
                .into(poster)
            movieName.text=movieModel[position].movieName
            genre.text=movieModel[position].movieGenre
            raiting.text=movieModel[position].rate
            if (movieModel[position].viewed)
            {viewed.visibility=View.VISIBLE}
            else{viewed.visibility=View.INVISIBLE}
        }
    }

    inner class MovieViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val binding=MovieItemBinding.bind(itemView)
    }

}