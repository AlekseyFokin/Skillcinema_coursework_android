package org.sniffsnirr.skillcinema.ui.serial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.EpisodeItemBinding
import org.sniffsnirr.skillcinema.entities.serialinfo.Episode
import java.text.SimpleDateFormat
import java.util.Locale

// Адаптер для RV сезона сериала
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
            aboutEpisode.text = "Серия ${episode.episodeNumber}. ${episode.nameRu ?: ""}"
            timeEpisode.text = castDate(episode.releaseDate)
        }
    }

    private fun castDate(releasData: String?): String {
        var formattedDate = ""

        if (!releasData.isNullOrEmpty()) {
            val dateParser = SimpleDateFormat("yyyy-MM-dd")

            val date = dateParser.parse(releasData)
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
            formattedDate = dateFormatter.format(date)
        }

        return formattedDate
    }

    inner class EpizodeViewHolder(val binding: EpisodeItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}