package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
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
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.ButtonViewHolder
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.Companion
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.Companion.MOVIE
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.PosterViewHolder
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

class DialogMovieToCollectionAdapter( val onPlusCollectionClick: () -> Unit,): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contentList= mutableListOf<Pair<CollectionCountMovies,Boolean>>()

    fun setContent(newContent:List<Pair<CollectionCountMovies,Boolean>>){
        this.contentList=newContent.toMutableList()
    }

    inner class ButtonViewHolder(val binding: CollectionDialogItemButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.addCollectionItem// добавление новой коллекции
                .setOnClickListener {

                    onPlusCollectionClick()

                }
        }
    }

    inner class CollectionViewHolder(private val binding: CollectionDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collection: Pair<CollectionCountMovies, Boolean>,position: Int) {
            binding.collectionName.text = collection.first.name
            binding.collectionCount.text = collection.first.countMovies.toString()
            if (collection.second) {
                binding.checkbox.setImageResource(R.drawable.ic_checkbox_fill)
            } else {
                binding.checkbox.setImageResource(R.drawable.ic_checkbox_unfill)
            }

            binding.checkbox.setOnClickListener {//щелчек по чекбоксу коллекции
                contentList[position] = Pair(collection.first,!collection.second)
                notifyItemChanged(position)
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
        if (viewType == BUTTON) {
            val binding =
                CollectionDialogItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ButtonViewHolder(binding)
        } else {
            val binding =
                CollectionDialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CollectionViewHolder(binding)
        }
    }

    override fun getItemCount()=contentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == BUTTON) {
            (holder as DialogMovieToCollectionAdapter.ButtonViewHolder).bind()
        } else {
            (holder as DialogMovieToCollectionAdapter.CollectionViewHolder).bind(contentList[position],position)
        }
    }

    companion object {
        const val COLLECTION = 0
        const val BUTTON = 1
    }
}
//emptyList<Pair<CollectionAdapter,Boolean>>()
