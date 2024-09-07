package org.sniffsnirr.skillcinema.ui.serial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.EpisodeItemBinding
import org.sniffsnirr.skillcinema.entities.serialinfo.Episode
import org.sniffsnirr.skillcinema.ui.movieman.filmography.FilmographyAdapter


class SerialSeasonAdapter : RecyclerView.Adapter<SerialSeasonAdapter.EpizodeViewHolder>() {

    private var episodeList: List<Episode> = emptyList()

    fun setData(episodeList: List<Episode>) {
        this.episodeList = episodeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpizodeViewHolder {
        val binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpizodeViewHolder(binding)
    }

    override fun getItemCount() = episodeList.size

    override fun onBindViewHolder(holder: EpizodeViewHolder, position: Int) {
        val episode = episodeList[position]
        with(holder.binding) {
            aboutEpisode.text = "Серия ${episode.episodeNumber}. ${episode.nameRu}"
            timeEpisode.text = episode.releaseDate
        }
    }

    inner class EpizodeViewHolder(val binding: EpisodeItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}