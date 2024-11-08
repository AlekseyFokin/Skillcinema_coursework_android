package org.sniffsnirr.skillcinema.ui.search.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.SearchOptionRvItemBinding
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre

class GenreOptionAdapter(val  genres: List<Genre>) :
    RecyclerView.Adapter<GenreOptionAdapter.GenreViewHolder>() {

    inner class GenreViewHolder(val binding: SearchOptionRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding =SearchOptionRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GenreViewHolder(binding)
    }

    override fun getItemCount()=genres.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.binding.itemText.text=genre.genre
    }
}