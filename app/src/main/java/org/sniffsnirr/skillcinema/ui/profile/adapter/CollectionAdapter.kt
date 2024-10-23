package org.sniffsnirr.skillcinema.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.CollectionItemBinding
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment.Companion.ID_FAVORITE_COLLECTION
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment.Companion.ID_WANT_TO_SEE_COLLECTION


class CollectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var collections = emptyList<CollectionDBO>().toMutableList()

    fun setData(collections: List<CollectionDBO>?) {
        this.collections = collections?.toMutableList()?:emptyList<CollectionDBO>().toMutableList()
        notifyDataSetChanged()
    }

    inner class ButtonViewHolder(val binding: CollectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collectionModel: CollectionDBO) {
            binding.closeBtn.setOnClickListener {
                onDeleteCollectionClick(collectionModel.id)
            }
            binding.openCollectionBtn.setOnClickListener {
                onOpenCollectionClick(collectionModel.id)
            }

            when (collectionModel.id){
                ID_FAVORITE_COLLECTION -> binding.collectionLabel.setImageResource(R.drawable.ic_heart)
                ID_WANT_TO_SEE_COLLECTION ->binding.collectionLabel.setImageResource(R.drawable.ic_bookmark)
                else -> binding.collectionLabel.setImageResource(R.drawable.ic_profile_black)
            }

            binding.collectionName.text=collectionModel.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ButtonViewHolder(binding)
    }

    override fun getItemCount()=collections.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ButtonViewHolder).bind(collections!![position])
    }
}