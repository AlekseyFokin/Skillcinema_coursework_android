package org.sniffsnirr.skillcinema.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.BannerItemBinding
import org.sniffsnirr.skillcinema.databinding.ParentItemBinding
import org.sniffsnirr.skillcinema.ui.home.model.MainModel


// Адаптер для rv верхнего уровня - отличает два типа item  - баннер и коллекции фильмов
class MainAdapter(
    private val mainModelList: List<MainModel>,
    val onCollectionClick: (MainModel) -> Unit,
    val onMovieClick: (Int?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == BANNER) {
            val binding =
                BannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return BannerViewHolder(binding)
        } else {
            val binding =
                ParentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MainModelRVViewHolder(binding)
        }
    }

    override fun getItemCount() = mainModelList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == BANNER) {
            (holder as BannerViewHolder).bind()
        } else {
            (holder as MainModelRVViewHolder).bind(mainModelList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mainModelList[position].banner) {
            BANNER
        } else {
            MOVIES
        }
    }

    inner class BannerViewHolder(binding: BannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() = Unit
    }

    inner class MainModelRVViewHolder(private val binding: ParentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: MainModel) {
            binding.apply {
                moviesCategory.text = dataItem.category
                val movieAdapter = MovieAdapter(dataItem.movieRVModelList,
                    { collectionModel -> onCollectionClick(dataItem) },
                    { idMovie -> onMovieClick(idMovie) })
                childRv.adapter = movieAdapter
            }
            binding.giveMeAll.setOnClickListener { onCollectionClick(dataItem) }
        }
    }

    companion object {
        const val BANNER = 0
        const val MOVIES = 1
    }
}