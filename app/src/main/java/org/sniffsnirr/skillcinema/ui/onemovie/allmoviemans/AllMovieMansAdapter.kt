package org.sniffsnirr.skillcinema.ui.onemovie.allmoviemans

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.ActorMoviemanBinding
import org.sniffsnirr.skillcinema.entities.staff.Staff

// адаптер для rv содрежащего всех актеров или кинематографистов
class AllMovieMansAdapter(val onMoviemanClick: (Int) -> Unit): RecyclerView.Adapter<AllMovieMansAdapter.AllMovieMansHolder>() {

    private var moviemanList: List<Staff> = emptyList()

    fun setData(moviemanList: List<Staff>) {
        this.moviemanList = moviemanList
        notifyDataSetChanged()
    }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMovieMansHolder {
        return AllMovieMansHolder(ActorMoviemanBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount()=moviemanList.size

    override fun onBindViewHolder(holder: AllMovieMansHolder, position: Int) {
        val item = moviemanList.getOrNull(position)
        item?.let {
            holder.binding.actorName.text=it.nameRu
            holder.binding.rule.text=it.description
            Glide
                .with(holder.binding.actorPhoto.context)
                .load(it.posterUrl)
                .into(holder.binding.actorPhoto)
        }
        holder.binding.root.setOnClickListener {  onMoviemanClick(item?.staffId?:0)}

    }
    inner class AllMovieMansHolder(val binding: ActorMoviemanBinding): RecyclerView.ViewHolder(binding.root)
}