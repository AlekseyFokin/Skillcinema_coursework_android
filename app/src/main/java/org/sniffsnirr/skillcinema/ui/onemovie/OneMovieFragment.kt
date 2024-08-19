package org.sniffsnirr.skillcinema.ui.onemovie

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.databinding.FragmentOneMovieBinding
import org.sniffsnirr.skillcinema.entities.Country
import org.sniffsnirr.skillcinema.entities.Genre
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.MoviemenAdapter
import java.util.Locale

@AndroidEntryPoint
class OneMovieFragment : Fragment() {
    private var _binding: FragmentOneMovieBinding? = null
    val binding get() = _binding!!

    private val viewModel: OneMovieViewModel by viewModels()

    private val actorsAdapter = MoviemenAdapter()

    private val moviemenAdapter = MoviemenAdapter()
    //  private var idMovie: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMovie = arguments?.getInt(HomeFragment.ID_MOVIE) ?: 0
        viewModel.setIdMovie(idMovie)
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle("")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {// загрузка инфо о фильме
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieInfo.collect {
                    with(binding)
                    {
                        countryDurationBond.text = concatCountryDurationBond(
                            it?.countries,
                            it?.filmLength,
                            it?.ratingAgeLimits
                        )
                        desc.text = it?.description
                        desc.setIsExpanded(false)
                        desc.setCollapsedLines(3)
                        desc.setReadMoreText("")
                        desc.setReadLessText("")
                        rateName.text = concatRateNameRu(it?.ratingKinopoisk, it?.nameRu)
                        shortDesc.text = it?.shortDescription
                        yearGenre.text = concatYearGenre(it?.year, it?.genres)
                        Glide
                            .with(mainPoster.context)
                            .load(it?.posterUrl)
                            .into(mainPoster)

                        Glide
                            .with(logo.context)
                            .load(it?.logoUrl)
                            .into(logo)
                    }

                }
            }
        }
        binding.actorsRv.setHasFixedSize(true)
        binding.actorsRv.layoutManager=
            GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL,false)
            binding.actorsRv.adapter=actorsAdapter
    viewModel.actorsInfo.onEach{
        actorsAdapter.setData(it.take(20))
        binding.numberOfActors.text="${it.size.toString()} >"
        Log.d("что дошло до фрагмента","${it.size}")
    }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.moviemenRv.setHasFixedSize(true)
        binding.moviemenRv.layoutManager=
            GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL,false)
        binding.moviemenRv.adapter=moviemenAdapter
        viewModel.movieMenInfo.onEach{
            moviemenAdapter.setData(it.take(20))
            binding.numberOfMoviemen.text="${it.size.toString()} >"
            Log.d("что дошло до фрагмента","${it.size}")
        }.launchIn(viewLifecycleOwner.lifecycleScope)
}

fun minutesToHour(minutes: Int?): String {
    if (minutes != null) {
        val hours = minutes / 60
        val minutes_out = minutes % 60
        return "$hours ч $minutes_out мин"
    } else {
        return ""
    }
}

fun ageLimit(string: String?): String {
    return if (!string.isNullOrEmpty()) {
        "${string.filter { it.isDigit() }}+"
    } else ""
}

fun concatCountryDurationBond(countries: List<Country>?, duration: Int?, bond: String?): String {
    val someCountries = countries?.joinToString(
        separator = ",",
        limit = 3,
        truncated = "..."
    )
    val durationInHour = minutesToHour(duration)
    val ageBond = ageLimit(bond)
    return "$someCountries, $durationInHour, $ageBond"
}

fun concatRateNameRu(rate: Double?, name: String?): String {
    return "${String.format(Locale.US, "%.1f", rate)} $name"
}

fun concatYearGenre(year: Int?, genres: List<Genre>?): String {
    val someGenres = genres?.joinToString(
        separator = ",",
        limit = 3,
        truncated = "..."
    )
    return "$year , $someGenres"
}

companion object {
    const val RV_1_NAME = "В фильме снимались"
    const val RV_2_NAME = "Над фильмом работали"
    const val RV_3_NAME = "Галерея"
    const val RV_4_NAME = "Похожие фильмы"
}
}