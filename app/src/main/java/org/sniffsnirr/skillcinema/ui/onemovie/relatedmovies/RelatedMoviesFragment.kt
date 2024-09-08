package org.sniffsnirr.skillcinema.ui.onemovie.relatedmovies

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentRelatedMoviesBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment

class RelatedMoviesFragment : Fragment() {

    private var _binding: FragmentRelatedMoviesBinding? = null
    val binding get() = _binding!!
    var movieName = ""
    private lateinit var arrayListOfBestMovies: ArrayList<MovieRVModel>

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
        binding.relatedMoviesRv.adapter= RelatedMovieAdapter(arrayListOfBestMovies){ idMovie->onMovieClick(idMovie)}
    }


    private fun onMovieClick(idMovie: Int?) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_relatedMoviesFragment_to_oneMovieFragment,
                bundle
            )
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    companion object{
        const val FRAGMENT_NAME="Фильмы, похожие на"
    }
}