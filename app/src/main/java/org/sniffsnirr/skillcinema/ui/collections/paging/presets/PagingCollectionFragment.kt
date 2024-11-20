package org.sniffsnirr.skillcinema.ui.collections.paging.presets

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
import org.sniffsnirr.skillcinema.databinding.FragmentPagingCollectionBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.PagingLoadStateAdapter
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE

// фрагмент для отображения фильмов одной коллекции ( пресета на стороне API напримео топ-250)
@AndroidEntryPoint
class PagingCollectionFragment : Fragment() {

    private val viewModel: PagingCollectionViewModel by viewModels()
    var _binding: FragmentPagingCollectionBinding? = null
    val binding get() = _binding!!
    private val pagedAdapter = PagingCollectionAdapter { idMovie -> onMovieClick(idMovie) }
    private var collectionName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val collectionType = arguments?.getCharSequence(HomeFragment.COLLECTION_TYPE)
        viewModel.collectionType = collectionType.toString()
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
                    )//передаю сигнал об изменении выше
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagingCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showActionBar()

        val myGridLayout = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        val footerAdapter = PagingLoadStateAdapter()
        val myAdapter = pagedAdapter.withLoadStateHeader(footerAdapter)

        binding.moviePagingCollectionRv.layoutManager = myGridLayout
        binding.moviePagingCollectionRv.adapter = myAdapter

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

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideActionBar()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    private fun onMovieClick(idMovie: Int?) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_pagingCollectionFragment_to_oneMovieFragment,
                bundle
            )
        }
    }

    companion object {
        const val RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY = "RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY"
        const val RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY = "RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY"
    }
}