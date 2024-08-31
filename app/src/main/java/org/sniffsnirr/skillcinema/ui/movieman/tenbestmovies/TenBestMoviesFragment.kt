package org.sniffsnirr.skillcinema.ui.movieman.tenbestmovies

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentPagingCompilationBinding
import org.sniffsnirr.skillcinema.databinding.FragmentTenBestMoviesBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment.Companion.BEST_MOVIES_LIST
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment.Companion.MOVIEMAN_NAME
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment.Companion.PHOTO_URL

class TenBestMoviesFragment : Fragment() {

    var _binding: FragmentTenBestMoviesBinding? = null
    val binding get() = _binding!!
    var moviemanName = ""
    lateinit var arrayListOfBestMovies: ArrayList<MovieRVModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviemanName = arguments?.getCharSequence(MoviemanFragment.MOVIEMAN_NAME).toString() ?: ""
        arrayListOfBestMovies = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(BEST_MOVIES_LIST)!!
        } else {
            arguments?.getParcelableArrayList(
                BEST_MOVIES_LIST,
                MovieRVModel::class.java
            )!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTenBestMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tenBestMoviesRv.setHasFixedSize(true)
        binding.tenBestMoviesRv.layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        binding.tenBestMoviesRv.adapter=TenBestMovieAdapter(arrayListOfBestMovies){idMovie->onMovieClick(idMovie)}
    }

    private fun onMovieClick(idMovie: Int?) {
        Log.d("ButtonClick", "$idMovie")
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_tenBestMoviesFragment_to_oneMovieFragment,
                bundle
            )
        }
    }

}