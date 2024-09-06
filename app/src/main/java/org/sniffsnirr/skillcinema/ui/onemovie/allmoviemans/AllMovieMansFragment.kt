package org.sniffsnirr.skillcinema.ui.onemovie.allmoviemans

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentAllMovieMansBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment.Companion.ID_STAFF

@AndroidEntryPoint
class AllMovieMansFragment : Fragment() {

    private var _binding:FragmentAllMovieMansBinding?=null
    val binding get()=_binding!!

    private val viewModel: AllMovieMansViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMovie = arguments?.getInt(HomeFragment.ID_MOVIE) ?: 0
        val movieName=arguments?.getCharSequence(OneMovieFragment.MOVIE_NAME) ?: ""
        val typeOfMoviemans=arguments?.getBoolean(OneMovieFragment.ACTORS_OR_MOVIEMANS)?:true
        viewModel.loadAllMoviemanByMovieId(idMovie,typeOfMoviemans)
        (activity as MainActivity).showActionBar()
        if (typeOfMoviemans) {
        (activity as MainActivity).setActionBarTitle("В фильме ${movieName} снимались" )}
        else {(activity as MainActivity).setActionBarTitle("Над фильмом ${movieName} раюотали" )}

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentAllMovieMansBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allmoviemanRv.setHasFixedSize(true)
        binding.allmoviemanRv.layoutManager =
            LinearLayoutManager(requireContext(),  GridLayoutManager.VERTICAL, false)
        val adapter=AllMovieMansAdapter({idStaff -> onMoviemanClick(idStaff)})
        binding.allmoviemanRv.adapter = adapter
        viewModel.movieMenInfo.onEach {//загрузка актеров
            adapter.setData(it)
         }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onMoviemanClick(idStaff: Int?) {
        Log.d("moviemanClick", "$idStaff")
        val bundle = Bundle()
        if (idStaff != null) {
            bundle.putInt(ID_STAFF, idStaff)

            findNavController().navigate(
                R.id.action_allMovieMansFragment_to_moviemanFragment,
                bundle
            )
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle("")
    }
}