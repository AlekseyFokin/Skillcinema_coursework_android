package org.sniffsnirr.skillcinema.ui.search

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentSearchBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.PagingLoadStateAdapter
import org.sniffsnirr.skillcinema.ui.collections.paging.compilations.PagingCompilationAdapter
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.ALL_TYPE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_COUNTRY
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_END_PERIOD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_END_RATE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_GENRE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_KEYWORD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_ONLY_UNVIEWED
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_START_PERIOD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_START_RATE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.SORT_DEFAULT

@AndroidEntryPoint
class SearchFragment : Fragment() {

    val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val pagedAdapter = SearchResultPagingAdapter()// { idMovie -> onMovieClick(idMovie) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.searchOptions.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(QUERYPARAMS_KEY, viewModel.queryParams.value)
            findNavController().navigate(
                R.id.action_navigation_search_to_allOptionsFragment
            )
        }

        binding.searchView.searchTextInput.addTextChangedListener {
            viewModel.setSearchMovieString(binding.searchView.searchTextInput.text.toString())

        }

        val footerAdapter = PagingLoadStateAdapter()
        val adapter = pagedAdapter.withLoadStateFooter(footerAdapter)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchResultRv.adapter = adapter
        binding.searchResultRv.layoutManager = gridLayoutManager

        viewModel.pagedMovies.onEach {
            pagedAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(FRAGMENT_REQUEST_KEY) { FRAGMENT_REQUEST_KEY, bundle ->

            val queryParams = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(FRAGMENT_BANDLE_KEY, QueryParams::class.java)
            } else {
                bundle.getParcelable(FRAGMENT_BANDLE_KEY)
            }
            if(queryParams!=null) {viewModel.setQueryParams(queryParams)}
        }
    }


    companion object {
        const val QUERYPARAMS_KEY = "QUERYPARAMS_KEY"
        const val FRAGMENT_REQUEST_KEY = "FRAGMENT_REQUEST_KEY"
        const val FRAGMENT_BANDLE_KEY = "FRAGMENT_BANDLE_KEY"
    }
}