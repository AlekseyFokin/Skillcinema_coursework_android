package org.sniffsnirr.skillcinema.ui.collections.premieres

import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentCollectionBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

@AndroidEntryPoint
class CollectionFragment : Fragment() {

    private val viewModel: CollectionViewModel by viewModels()
    var _binding: FragmentCollectionBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
        val collectionName = arguments?.getCharSequence(HomeFragment.COLLECTION_NAME)
        (activity as MainActivity).setActionBarTitle(collectionName.toString())
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
        var collectionModel: List<MovieRVModel>? = null
        collectionModel = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(HomeFragment.COLLECTION_MODEL)
        } else {
            arguments?.getParcelableArrayList(
                HomeFragment.COLLECTION_MODEL,
                MovieRVModel::class.java
            )
        }
        Log.d("collection", "${collectionModel?.size ?: "0"}")
        if (!collectionModel.isNullOrEmpty()) {
            val movieCollection = collectionModel.toList()
            Log.d("collection", "${movieCollection.size}")
            val adapter = CollectionAdapter(movieCollection) { idMovie -> onMovieClick(idMovie) }
            binding.movieCollectionRv.adapter = adapter
            binding.movieCollectionRv.setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideActionBar()
    }

    private fun onMovieClick(idMovie: Int?) {
        Log.d("ButtonClick", "$idMovie")
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