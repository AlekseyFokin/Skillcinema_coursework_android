package org.sniffsnirr.skillcinema.ui.profile.adapter

import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.ClearHistoryBinding
import org.sniffsnirr.skillcinema.databinding.CollectionItemBinding
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

class CollectionAdapter {

    inner class ButtonViewHolder(val binding: CollectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collectionModel: CollectionDBO) {
            binding.showMeAllBtn.setOnClickListener {
                onButtonClearAllClick()
            }
        }
    }
}