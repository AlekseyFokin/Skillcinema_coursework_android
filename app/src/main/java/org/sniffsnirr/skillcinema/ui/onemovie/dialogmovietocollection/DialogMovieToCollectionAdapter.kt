package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.CollectionDialogItemBinding
import org.sniffsnirr.skillcinema.databinding.CollectionDialogItemButtonBinding
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter
import org.sniffsnirr.skillcinema.ui.home.adapter.MovieAdapter.Companion.MOVIE

class DialogMovieToCollectionAdapter( val onPlusCollectionClick: () -> Unit,
    val setCurrentListCollectionWithMark:(List<Pair<CollectionCountMovies, Boolean>>)->Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var contentList= mutableListOf<Pair<CollectionCountMovies,Boolean>>()

    fun setContent(newContent:List<Pair<CollectionCountMovies,Boolean>>){
        this.contentList=newContent.toMutableList()
        notifyDataSetChanged()
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
                setCurrentListCollectionWithMark(contentList)
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

