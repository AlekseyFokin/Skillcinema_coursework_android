package org.sniffsnirr.skillcinema.ui.onemovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.databinding.GalleryItemBinding
import org.sniffsnirr.skillcinema.entities.images.Image


// адаптер для rv содрежащего изображения к фильму
class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.GalleryImageHolder>() {

    private var images: List<Image> = emptyList()

    fun setData(images: List<Image>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageHolder {
        return GalleryImageHolder(
            GalleryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: GalleryImageHolder, position: Int) {
        val item = images.getOrNull(position)
        item?.let {
            Glide
                .with(holder.binding.galleryImage.context)
                .load(it.previewUrl)
                .into(holder.binding.galleryImage)
        }
    }

    inner class GalleryImageHolder(val binding: GalleryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}