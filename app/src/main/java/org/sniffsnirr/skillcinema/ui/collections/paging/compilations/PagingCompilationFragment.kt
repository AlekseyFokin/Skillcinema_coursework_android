package org.sniffsnirr.skillcinema.ui.collections.paging.compilations

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.databinding.FragmentPagingCollectionBinding
import org.sniffsnirr.skillcinema.databinding.FragmentPagingCompilationBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.PagingLoadStateAdapter
import org.sniffsnirr.skillcinema.ui.home.HomeFragment

@AndroidEntryPoint
class PagingCompilationFragment : Fragment() {

    private val viewModel: PagingCompilationViewModel by viewModels()
    var _binding: FragmentPagingCompilationBinding? = null
    val binding get() = _binding!!
    val pagedAdapter = PagingCompilationAdapter { string -> onMovieClick(string) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val collectionType = arguments?.getCharSequence(HomeFragment.COLLECTION_TYPE)
        val collectionCountry = arguments?.getInt(HomeFragment.COLLECTION_COUNTRY)
        val collectionGenre = arguments?.getInt(HomeFragment.COLLECTION_GENRE)
        viewModel.collectionType =
            Triple(collectionType?.toString() ?: "", collectionCountry ?: 0, collectionGenre ?: 0)
        (activity as MainActivity).showActionBar()
        val collectionName = arguments?.getCharSequence(HomeFragment.COLLECTION_NAME)
        (activity as MainActivity).setActionBarTitle(collectionName.toString())

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
        val footerAdapter=PagingLoadStateAdapter()
        val adapter=pagedAdapter.withLoadStateFooter(footerAdapter)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.moviePagingCollectionRv.adapter =adapter
        binding.moviePagingCollectionRv.layoutManager = gridLayoutManager

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        viewModel.pagedMovies.onEach {
            pagedAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideActionBar()
    }

    fun onMovieClick(string: String) {
        Log.d("ButtonClick", string)
    }
}