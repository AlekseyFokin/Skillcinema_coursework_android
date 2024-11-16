package org.sniffsnirr.skillcinema.ui.onemovie.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.StaggeredGalleryItemBinding
import org.sniffsnirr.skillcinema.entities.images.Image

class GalleryAdapter(private val widthDisplay: Int, val onPhotoClick: (String?) -> Unit) :
    PagingDataAdapter<Image, GalleryAdapter.ImageViewHolder>(
        DiffUtilCallback()
    ) {

    override fun onBindViewHolder(holder: GalleryAdapter.ImageViewHolder, position: Int) {

        val image = getItem(position)
        if (position % 3 == 0&&position<(itemCount-1)) {// каждая третья картинка на всю ширину экрана
            holder.binding.galleryImage.layoutParams.width =
                (widthDisplay - 56 * holder.binding.galleryImage.context.resources
                    .displayMetrics.density).toInt()
        }

        with(holder.binding) {
            com.bumptech.glide.Glide
                .with(galleryImage.context)
                .load(image?.previewUrl)
                .into(galleryImage)

        }
        holder.binding.galleryImage.setOnClickListener { onPhotoClick(image?.imageUrl) }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryAdapter.ImageViewHolder {
        val binding =
            StaggeredGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    inner class ImageViewHolder(val binding: StaggeredGalleryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DiffUtilCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
            oldItem.imageUrl == newItem.imageUrl

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
            oldItem == newItem
    }
}