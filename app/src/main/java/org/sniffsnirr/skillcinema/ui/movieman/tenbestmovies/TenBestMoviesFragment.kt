package org.sniffsnirr.skillcinema.ui.movieman.tenbestmovies

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
import org.sniffsnirr.skillcinema.databinding.FragmentTenBestMoviesBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment.Companion.BEST_MOVIES_LIST

//Фрагмент показа 10 лучших фильмов
class TenBestMoviesFragment : Fragment() {
    var _binding: FragmentTenBestMoviesBinding? = null
    val binding get() = _binding!!
    private var moviemanName = ""
    private lateinit var arrayListOfBestMovies: ArrayList<MovieRVModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
        moviemanName = arguments?.getCharSequence(MoviemanFragment.MOVIEMAN_NAME).toString()
        arrayListOfBestMovies = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(BEST_MOVIES_LIST)!!
        } else {
            arguments?.getParcelableArrayList(
                BEST_MOVIES_LIST,
                MovieRVModel::class.java
            )!!
        }
      }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("$moviemanName. $BEST_MOVIES")
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
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_tenBestMoviesFragment_to_oneMovieFragment,
                bundle
            )
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    companion object{
        const val BEST_MOVIES="Лучшие фильмы"
    }

}