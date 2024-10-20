package org.sniffsnirr.skillcinema.ui.onemovie.relatedmovies

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentRelatedMoviesBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment

class RelatedMoviesFragment : Fragment() {

    private var _binding: FragmentRelatedMoviesBinding? = null
    val binding get() = _binding!!
    var movieName = ""
    private lateinit var arrayListOfBestMovies: ArrayList<MovieRVModel>
    val relatedMovieAdapter=RelatedMovieAdapter{ idMovie,position->onMovieClick(idMovie,position)}
    var possiblyEditablePosition=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieName = arguments?.getCharSequence(OneMovieFragment.MOVIE_NAME).toString()
        arrayListOfBestMovies = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(OneMovieFragment.RELATED_MOVIES_LIST)!!
        } else {
            arguments?.getParcelableArrayList(
                OneMovieFragment.RELATED_MOVIES_LIST,
                MovieRVModel::class.java
            )!!
        }
    }

    override fun onResume() {
                super.onResume()
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle("$FRAGMENT_NAME $movieName")

        setFragmentResultListener(RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY, bundle ->
            if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
                if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
                    relatedMovieAdapter.updateMovieRVModel(possiblyEditablePosition)
                    Log.d("Update", "Update_DONE!!!")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRelatedMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.relatedMoviesRv.setHasFixedSize(true)
        binding.relatedMoviesRv.layoutManager=
            GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
        binding.relatedMoviesRv.adapter=relatedMovieAdapter
        relatedMovieAdapter.setMovieList(arrayListOfBestMovies)
    }


    private fun onMovieClick(idMovie: Int?,position:Int) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_relatedMoviesFragment_to_oneMovieFragment,
                bundle
            )
        }
        possiblyEditablePosition=position
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    companion object{
        const val FRAGMENT_NAME="Фильмы, похожие на"
    }
}