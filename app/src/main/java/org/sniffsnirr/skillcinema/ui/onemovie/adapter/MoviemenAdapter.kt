package org.sniffsnirr.skillcinema.ui.onemovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.ActorMoviemanBinding
import org.sniffsnirr.skillcinema.entities.staff.Staff

class MoviemenAdapter() :
    RecyclerView.Adapter<MoviemenAdapter.ActorViewHolder>() {

    private var actors: List<Staff> = emptyList()
    fun setData(actors: List<Staff>) {
        this.actors = actors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
       return ActorViewHolder(ActorMoviemanBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = actors.size

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val item = actors.getOrNull(position)
        with(holder.binding) {
            actorName.text=item?.nameRu
            rule.text=item?.description
            item?.let {
                Glide
                    .with(actorPhoto.context)
                    .load(it.posterUrl)
                    .into(actorPhoto)
            }
        }
    }

    inner class ActorViewHolder(val binding: ActorMoviemanBinding) :
        RecyclerView.ViewHolder(binding.root)
}