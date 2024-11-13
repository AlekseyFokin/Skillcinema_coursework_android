package org.sniffsnirr.skillcinema.ui.search

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentSearchBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.PagingLoadStateAdapter
import org.sniffsnirr.skillcinema.ui.collections.paging.compilations.PagingCompilationAdapter
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE
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

    private val pagedAdapter = SearchResultPagingAdapter { idMovie -> onMovieClick(idMovie) }

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

        // скрыть все индикаторы loadstate

        binding.progressbarAppend.visibility = View.GONE
        binding.progressbarRefresh.visibility = View.GONE
        binding.haveNoResult.visibility = View.GONE

        binding.searchOptions.setOnClickListener {//перейти на экран с опциями
            val bundle = Bundle()
            bundle.putParcelable(QUERYPARAMS_KEY, viewModel.queryParams.value)
            findNavController().navigate(
                R.id.action_navigation_search_to_allOptionsFragment, bundle
            )
        }

        binding.searchTextInput.addTextChangedListener {// ввод в поисковую строку
            viewModel.setSearchMovieString(binding.searchTextInput.text.toString())
        }

        //  val footerAdapter = PagingLoadStateAdapter()
        //  val adapter = pagedAdapter.withLoadStateFooter(footerAdapter)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchResultRv.adapter = pagedAdapter
        binding.searchResultRv.layoutManager = gridLayoutManager

        viewLifecycleOwner.lifecycleScope.launch {// загрузка RV коллекций
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filtredflow.collect {
                    pagedAdapter.submitData(it)
                }
            }
        }

        lifecycleScope.launch {
            pagedAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressbarRefresh.visibility = View.GONE
                binding.progressbarAppend.visibility = View.GONE
                binding.haveNoResult.visibility = View.GONE

                when (loadStates.refresh) {
                    is LoadState.Loading -> binding.progressbarRefresh.visibility = View.VISIBLE
                    is LoadState.Error -> Toast.makeText(
                        requireContext(),
                        "Ошибка загрузки",
                        Toast.LENGTH_LONG
                    ).show()

                    is LoadState.NotLoading -> if (pagedAdapter.itemCount < 1) {
                        binding.haveNoResult.visibility = View.VISIBLE
                    }

                }
                when (loadStates.append) {
                    is LoadState.Loading -> binding.progressbarAppend.visibility = View.VISIBLE
                    is LoadState.Error -> Toast.makeText(
                        requireContext(),
                        "Ошибка добавления",
                        Toast.LENGTH_LONG
                    ).show()

                    is LoadState.NotLoading -> if (pagedAdapter.itemCount < 1) {
                        binding.haveNoResult.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()// получение параметров запроса из фрагментов с обпциями поиска
        setFragmentResultListener(FRAGMENT_REQUEST_KEY) { FRAGMENT_REQUEST_KEY, bundle ->
            val queryParams = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(FRAGMENT_BANDLE_KEY, QueryParams::class.java)
            } else {
                bundle.getParcelable(FRAGMENT_BANDLE_KEY)
            }
            if (queryParams != null) {
                viewModel.setQueryParams(queryParams)
            }
        }
    }

    private fun onMovieClick(idMovie: Int?) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_navigation_search_to_oneMovieFragment,
                bundle
            )
        }
    }


    companion object {
        const val QUERYPARAMS_KEY = "QUERYPARAMS_KEY"
        const val FRAGMENT_REQUEST_KEY = "FRAGMENT_REQUEST_KEY"
        const val FRAGMENT_BANDLE_KEY = "FRAGMENT_BANDLE_KEY"
    }
}