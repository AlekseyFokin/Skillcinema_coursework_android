package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentGenreOptionBinding
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel
import org.sniffsnirr.skillcinema.ui.search.options.AllOptionsFragment.Companion

@AndroidEntryPoint
class GenreOptionFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels({requireParentFragment()})
    var _binding:FragmentGenreOptionBinding?= null
    val binding get()=_binding!!
    private val genreAdapter=GenreOptionAdapter{genre->onGenreClick(genre)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.genreRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.genreRv.setHasFixedSize(true)
        binding.genreRv.adapter =genreAdapter
        val dividerItemDecorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        dividerItemDecorator.setDrawable(resources.getDrawable(R.drawable.line_for_rv))
        binding.genreRv.addItemDecoration(dividerItemDecorator)

        viewLifecycleOwner.lifecycleScope.launch {// загрузка списка жанров
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genres.collect {
                    genreAdapter.setGenreList(it)
                }
            }
        }

        binding.searchView.searchTextInput.addTextChangedListener { //передача поисковой строки
            viewModel.setGenreSearchString(binding.searchView.searchTextInput.text.toString())
            viewModel.onChangeGenreSearchString()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle(NAME_FRAGMENT)
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    override fun onDestroy() {
        super.onDestroy()
    //    (activity as MainActivity).hideActionBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {_binding=FragmentGenreOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    fun onGenreClick(newGenre: Genre){
       viewModel.setGenre(newGenre.genre)
    }


    companion object{
        const val NAME_FRAGMENT="Жанр"
    }
}