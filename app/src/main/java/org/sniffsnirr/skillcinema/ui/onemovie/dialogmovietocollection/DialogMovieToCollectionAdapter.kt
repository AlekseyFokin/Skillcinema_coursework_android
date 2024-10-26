package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.CollectionDialogItemBinding
import org.sniffsnirr.skillcinema.databinding.CollectionDialogItemButtonBinding
import org.sniffsnirr.skillcinema.databinding.MovieItemBinding
import org.sniffsnirr.skillcinema.databinding.ShowMeAllBinding
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.Companion
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.Companion.MOVIE
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

class DialogMovieToCollectionAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contentList= mutableListOf<Pair<CollectionCountMovies,Boolean>>()

    fun setContent(newContent:List<Pair<CollectionCountMovies,Boolean>>){
        this.contentList=newContent.toMutableList()
    }

    inner class ButtonViewHolder(val binding: CollectionDialogItemButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.addCollectionBtn
                .setOnClickListener {

                }
        }
    }

    inner class CollectionViewHolder(private val binding: CollectionDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collection: Pair<CollectionCountMovies, Boolean>) {
            binding.collectionName.text = collection.first.name
            binding.collectionCount.text = collection.first.countMovies.toString()
            if (collection.second) {
                binding.checkbox.setImageResource(R.drawable.ic_checkbox_fill)
            } else {
                binding.checkbox.setImageResource(R.drawable.ic_checkbox_unfill)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (position==contentList.size-1) {
            MovieAdapter.BUTTON
        } else {
            MOVIE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount()=contentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val COLLECTION = 0
        const val BUTTON = 1
    }
}
//emptyList<Pair<CollectionAdapter,Boolean>>()
