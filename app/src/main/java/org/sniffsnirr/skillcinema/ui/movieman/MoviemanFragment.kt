package org.sniffsnirr.skillcinema.ui.movieman

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentMoviemanBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment

@AndroidEntryPoint
class MoviemanFragment : Fragment() {

    private var _binding:FragmentMoviemanBinding?= null
    val binding get() = _binding!!

    private val viewModel: MoviemanViewModel by viewModels()

    private var photoURL=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMovieman = arguments?.getInt(OneMovieFragment.ID_STAFF)?:0
        viewModel.getBestMovies(idMovieman)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentMoviemanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.moviemanInfo.onEach {//
            binding.nameMovieman.text=it?.nameRu
            binding.profOfMovieman.text=it?.profession
            photoURL=it?.posterUrl?:""
            Glide
                    .with(binding.photoMovieman.context)
                    .load(it?.posterUrl)
                    .into(binding.photoMovieman)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.bestMoviesRv.setHasFixedSize(true)
        binding.bestMoviesRv.layoutManager =
            LinearLayoutManager(requireContext(),GridLayoutManager.HORIZONTAL, false)

        viewModel.bestMovies.onEach {// загрузка кенематографистов
            binding.bestMoviesRv.adapter = BestMovieAdapter(it,{ onCollectionClick()},{ idMovie ->onMovieClick(idMovie)})
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.numberMovies.onEach {
            binding.numberOfMovies.text="$it фильма"
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.photoMovieman.setOnClickListener {
            val bundle = Bundle()

                bundle.putCharSequence(PHOTO_URL,photoURL )
                findNavController().navigate(
                    R.id.action_moviemanFragment_to_moviemanPhotoFragment,
                    bundle
                )
            }


    }

    private fun onMovieClick(idMovie: Int?) {
        Log.d("ButtonClick", "$idMovie")
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_moviemanFragment_to_oneMovieFragment,
                bundle
            )
        }
    }

    private fun onCollectionClick() {
        //Log.d("ButtonClick", "$idMovie")
        //val bundle = Bundle()
        //if (idMovie != null) {
        //    bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
        //    findNavController().navigate(
        //        R.id.action_moviemanFragment_to_oneMovieFragment,
         //       bundle
         //   )
       // }
    }

    companion object{
        const val PHOTO_URL="PHOTO_URL"
    }
}