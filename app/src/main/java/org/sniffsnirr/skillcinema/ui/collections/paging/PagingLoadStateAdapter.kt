package org.sniffsnirr.skillcinema.ui.collections.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.ProgressForPagingBinding

//отображение прогрессбара для списка фильмов с пагинацией
class PagingLoadStateAdapter : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = Unit

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            ProgressForPagingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }
}

class LoadStateViewHolder(binding: ProgressForPagingBinding) : RecyclerView.ViewHolder(binding.root)