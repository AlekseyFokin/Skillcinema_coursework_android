package org.sniffsnirr.skillcinema.ui.onemovie

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.GalleryAdapter
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.MoviemenAdapter
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.RelatedMoviesAdapter
import java.util.Locale

@AndroidEntryPoint
class OneMovieFragment : Fragment() {
    private var _binding: FragmentOneMovieBinding? = null
    val binding get() = _binding!!

    private val viewModel: OneMovieViewModel by viewModels()

    private val actorsAdapter = MoviemenAdapter()

    private val moviemenAdapter = MoviemenAdapter()

    private val galleryAdapter = GalleryAdapter()

    private val relatedMoviesAdapter = RelatedMoviesAdapter()

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
                        desc.setText(it?.description ?: "")
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
        binding.actorsRv.layoutManager =
            GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false)
        binding.actorsRv.adapter = actorsAdapter

        viewModel.actorsInfo.onEach {//загрузка актеров
            actorsAdapter.setData(it.take(20))
            binding.numberOfActors.text = "${it.size} >"
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.moviemenRv.setHasFixedSize(true)
        binding.moviemenRv.layoutManager =
            GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false)
        binding.moviemenRv.adapter = moviemenAdapter
        viewModel.movieMenInfo.onEach {// загрузка кенематографистов
            moviemenAdapter.setData(it.take(20))
            binding.numberOfMoviemen.text = "${it.size} >"
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.galleryRv.setHasFixedSize(true)
        binding.galleryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.galleryRv.adapter = galleryAdapter
        viewModel.gallery.onEach {// загрузка изображений к фильму
            galleryAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.relatedMoviesRv.setHasFixedSize(true)
        binding.relatedMoviesRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.relatedMoviesRv.adapter = relatedMoviesAdapter
        viewModel.relatedMovies.onEach {// загрузка похожих фильмов
            relatedMoviesAdapter.setData(it.take(20))
            binding.numberOfRelatedMovies.text = "${it.size} >"
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun minutesToHour(minutes: Int?): String { //перевод минут в часы и минуты
        if (minutes != null) {
            val hours = minutes / 60
            val minutesOut = minutes % 60
            return "$hours ч $minutesOut мин"
        } else {
            return ""
        }
    }

    private fun ageLimit(string: String?): String {// конвертирование допустимого возраста
        return if (!string.isNullOrEmpty()) {
            "${string.filter { it.isDigit() }}+"
        } else ""
    }

    private fun concatCountryDurationBond(// конкатинация стран-производителей, продолжительности фильма и ограничения по возрасту
        countries: List<Country>?,
        duration: Int?,
        bond: String?
    ): String {
        val someCountries = countries?.joinToString(
            separator = ",",
            limit = 3,
            truncated = "..."
        )
        val durationInHour = minutesToHour(duration)
        val ageBond = ageLimit(bond)
        return "$someCountries, $durationInHour, $ageBond"
    }

    private fun concatRateNameRu(
        rate: Double?,
        name: String?
    ): String {// конкатинация рейтинга и названия фильма
        return "${String.format(Locale.US, "%.1f", rate)} $name"
    }

    private fun concatYearGenre(
        year: Int?,
        genres: List<Genre>?
    ): String {// конкатинация года выпуска и жанров
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

        const val STILL = "Кадры"
        const val SHOOTING = "Изображения со съемок"
        const val POSTER = "Постеры"
        const val FAN_ART = "Фан-арты"
        const val PROMO = "Промо"
        const val CONCEPT = "Концепт-арты"
        const val WALLPAPER = "Обои"
        const val COVER = "Обложки"
        const val SCREENSHOT = "Скриншоты"
    }
}