package org.sniffsnirr.skillcinema.ui.onemovie.allmoviemans

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentAllMovieMansBinding
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment.Companion.ID_STAFF

// Фрагмент содрежащий всех актеров или кинематографистов
@AndroidEntryPoint
class AllMovieMansFragment : Fragment() {

    private var _binding:FragmentAllMovieMansBinding?=null
    val binding get()=_binding!!

    private val viewModel: AllMovieMansViewModel by viewModels()
    private var typeOfMoviemans=true
    var movieName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMovie = arguments?.getInt(HomeFragment.ID_MOVIE) ?: 0
        movieName=arguments?.getCharSequence(OneMovieFragment.MOVIE_NAME).toString()
        typeOfMoviemans=arguments?.getBoolean(OneMovieFragment.ACTORS_OR_MOVIEMANS)?:true
        viewModel.loadAllMoviemanByMovieId(idMovie,typeOfMoviemans)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentAllMovieMansBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (typeOfMoviemans) {
            (activity as MainActivity).setActionBarTitle("$FRAGMENT_NAME_ACTOR_1 $movieName $FRAGMENT_NAME_ACTOR_2" )}
        else {(activity as MainActivity).setActionBarTitle("$FRAGMENT_NAME_MOVIEMAN_1 $movieName $FRAGMENT_NAME_MOVIEMAN_2" )}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showActionBar()

        binding.allmoviemanRv.setHasFixedSize(true)
        binding.allmoviemanRv.layoutManager =
            LinearLayoutManager(requireContext(),  GridLayoutManager.VERTICAL, false)
        val adapter=AllMovieMansAdapter { idStaff -> onMoviemanClick(idStaff) }
        binding.allmoviemanRv.adapter = adapter
        viewModel.movieMenInfo.onEach {//загрузка актеров
            adapter.setData(it)
         }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {// ожидание ошибки
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect { _ ->
                    BottomSheetErrorFragment().show(parentFragmentManager, "errordialog")
                }
            }
        }
    }

    private fun onMoviemanClick(idStaff: Int?) {
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

    companion object{
        const val FRAGMENT_NAME_ACTOR_1="В фильме"
        const val FRAGMENT_NAME_ACTOR_2="снимались"

        const val FRAGMENT_NAME_MOVIEMAN_1="Над фильмом"
        const val FRAGMENT_NAME_MOVIEMAN_2="работали"
    }
}