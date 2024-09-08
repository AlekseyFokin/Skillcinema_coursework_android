package org.sniffsnirr.skillcinema.ui.collections.premieres

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentCollectionBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE

// фрагмент для отображения премьер
@AndroidEntryPoint
class CollectionFragment : Fragment() {

    private val viewModel: CollectionViewModel by viewModels()
    var _binding: FragmentCollectionBinding? = null
    val binding get() = _binding!!
    var collectionName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
        collectionName = arguments?.getCharSequence(HomeFragment.COLLECTION_NAME).toString()
         viewModel.loadPremiers()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle(collectionName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.premierMovies.onEach {
            if (!it.isNullOrEmpty()) {
                val premierMoviesWithoutHeader = it.filter { it.isButton == false }
                val adapter = CollectionAdapter(premierMoviesWithoutHeader) { idMovie ->
                    onMovieClick(idMovie)
                }
                binding.movieCollectionRv.adapter = adapter
                binding.movieCollectionRv.setHasFixedSize(true)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
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
                R.id.action_collectionFragment_to_oneMovieFragment,
                bundle
            )
        }
    }
}