package org.sniffsnirr.skillcinema.ui.collections.paging.compilations

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentPagingCompilationBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.PagingLoadStateAdapter
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE

// фрагмент для отображения фильмов определенной компиляции (страна/ жанр)
@AndroidEntryPoint
class PagingCompilationFragment : Fragment() {

    private val viewModel: PagingCompilationViewModel by viewModels()
    var _binding: FragmentPagingCompilationBinding? = null
    val binding get() = _binding!!
    private val pagedAdapter = PagingCompilationAdapter { idMovie -> onMovieClick(idMovie) }
    private var collectionName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val collectionType = arguments?.getCharSequence(HomeFragment.COLLECTION_TYPE)
        val collectionCountry = arguments?.getInt(HomeFragment.COLLECTION_COUNTRY)
        val collectionGenre = arguments?.getInt(HomeFragment.COLLECTION_GENRE)
        viewModel.collectionType =
            Triple(collectionType?.toString() ?: "", collectionCountry ?: 0, collectionGenre ?: 0)
        (activity as MainActivity).showActionBar()
        collectionName = arguments?.getCharSequence(HomeFragment.COLLECTION_NAME).toString()
    }

    override fun onResume() {// если при восстановлении фрагмента получен сигнал об изменении данных - обновить состояние и передать выше
        super.onResume()
        (activity as MainActivity).setActionBarTitle(collectionName)
        setFragmentResultListener(RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY, bundle ->
            if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
                if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
                    pagedAdapter.refresh()
                    setFragmentResult(
                        HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY,
                        bundleOf(HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY to true)
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagingCompilationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val footerAdapter = PagingLoadStateAdapter()
        val adapter = pagedAdapter.withLoadStateFooter(footerAdapter)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.moviePagingCollectionRv.adapter = adapter
        binding.moviePagingCollectionRv.layoutManager = gridLayoutManager

        viewModel.pagedMovies.onEach {
            pagedAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launch {// обработка ошибок пейджинга
            pagedAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Error||loadStates.append is LoadState.Error) {
                    BottomSheetErrorFragment().show(parentFragmentManager, "errordialog")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as MainActivity).hideActionBar()
    }

    private fun onMovieClick(idMovie: Int?) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_pagingCompilationFragment_to_oneMovieFragment,
                bundle
            )
        }
    }
}