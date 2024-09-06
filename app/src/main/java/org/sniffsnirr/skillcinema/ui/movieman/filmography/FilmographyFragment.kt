package org.sniffsnirr.skillcinema.ui.movieman.filmography

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentFilmographyBinding
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.movieman.BestMovieAdapter
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment


@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private val viewModel: FilmographyViewModel by viewModels()

    private var _binding: FragmentFilmographyBinding? = null
    val binding get() = _binding!!
    private var staffId = 0
    private var movieManSex = "MALE"
    private val  filmographyAdapter=FilmographyAdapter{idMovie-> onMovieClick(idMovie)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        staffId = arguments?.getInt(MoviemanFragment.MOVIEMAN_ID) ?: 0
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle("Фильмография")
        viewModel.getMoviesByProfessionKey(staffId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.moviemanName.onEach {
            binding.moviemanName.text = it.first
            movieManSex = it.second
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // собрать chipGroup
        viewModel.moviesByProfessionKey.onEach {
            it.forEach { item ->
                val chip:Chip = View.inflate(binding.filmographyChipgroup.context, R.layout.chip_templ, null) as Chip
                var label = SET_OF_PROFESSION_KEY.get(item.key)?:""

                if (label.isNullOrEmpty()) {
                    label = "Прочее"
                }
                if ((label == "Актер") && (movieManSex == "FEMALE")) {
                    label = "Актриса"
                }
                label = "$label   ${item.value.size}"
                val string = SpannableString(label)
                string.setSpan(
                    RelativeSizeSpan(0.7f),
                    string.lastIndexOf(" "),
                    string.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                chip.text = string
                val movieIdList= mutableListOf<Int>()
                item.value.map { film->movieIdList.add(film.filmId) }
                chip.setOnClickListener {
                    viewModel.getMoviesByListId(movieIdList)
                }
                binding.filmographyChipgroup.addView(chip)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.moviesRv.setHasFixedSize(true)
        binding.moviesRv.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.VERTICAL, false)
        binding.moviesRv.adapter =filmographyAdapter

        viewModel.moviesByListID.onEach {// загрузка фильмов с участием актера с определенным profKey
            filmographyAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isLoading.onEach {
          if (it) {binding.frameProgress.visibility=View.VISIBLE
          Log.d("progressbar", "on")}
            else{binding.frameProgress.visibility=View.INVISIBLE
              Log.d("progressbar", "off")}
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onMovieClick(idMovie: Int?) {
        Log.d("ButtonClick", "$idMovie")
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_filmographyFragment_to_oneMovieFragment,
                bundle
            )
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle("")
    }

    companion object {
        val SET_OF_PROFESSION_KEY = mapOf(
            Pair("ACTOR", "Актер"),
            Pair("HRONO_TITR_MALE", "Актер дубляжа"),
            Pair("EDITOR", "Редактор"),
            Pair("WRITER", "Сценарист"),
            Pair("HIMSELF", "Играет самого себя"),
            Pair("PRODUCER", "Продюсер"),
            Pair("DIRECTOR", "Режисер"),
            Pair("OPERATOR", "Оператор"),
            Pair("COMPOSER", "Специалист по визуальным эффектам"),
            Pair("HERSELF", "Играет саму себя"),
            Pair("HRONO_TITR_FEMALE", "Актриса дубляжа"),
            Pair("DESIGN","Дизайнер")
        )
    }
}
