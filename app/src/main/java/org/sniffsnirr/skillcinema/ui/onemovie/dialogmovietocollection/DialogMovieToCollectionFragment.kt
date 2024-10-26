package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentAddMovieToCollectionBinding
import org.sniffsnirr.skillcinema.ui.collections.premieres.CollectionAdapter
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment

class DialogMovieToCollectionFragment : Fragment() {

    private val viewModel: DialogMovieToCollectionViewModel by viewModels()
    var _binding: FragmentAddMovieToCollectionBinding? = null
    val binding get() = _binding!!
    val collectionListForAddMovie = mutableListOf<Int>()
    val adapter = DialogMovieToCollectionAdapter()

    var movieRVModel: MovieRVModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            movieRVModel =
                arguments?.getParcelable(OneMovieFragment.MOVIE, MovieRVModel::class.java)
        } else {
            movieRVModel = arguments?.getParcelable<MovieRVModel>(OneMovieFragment.MOVIE)
        }
        viewModel.loadCollections(movieRVModel?.kinopoiskId?.toLong()?:0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMovieToCollectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.poster
        binding.genre.text=movieRVModel?.movieGenre?:""
        binding.movieName.text=movieRVModel?.movieName?:""
        binding.raiting.text=movieRVModel?.rate

        binding.collectionRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.collectionRv.adapter = adapter
        binding.collectionRv.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {// загрузка RV коллекций
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectionsForRV.collect {
                    if (!it.isNullOrEmpty()) {
                        adapter.setData(it)
                    }
                }
            }
        }
    }




}