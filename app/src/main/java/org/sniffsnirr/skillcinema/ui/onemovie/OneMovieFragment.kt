package org.sniffsnirr.skillcinema.ui.onemovie

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentOneMovieBinding
import org.sniffsnirr.skillcinema.entities.Country
import org.sniffsnirr.skillcinema.entities.Genre
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.GalleryAdapter
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.MoviemenAdapter
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.RelatedMoviesAdapter
import org.sniffsnirr.skillcinema.usecases.Reduction
import java.util.Locale

@AndroidEntryPoint
class OneMovieFragment : Fragment() {
    private var _binding: FragmentOneMovieBinding? = null
    val binding get() = _binding!!

    private val viewModel: OneMovieViewModel by viewModels()

    private val actorsAdapter = MoviemenAdapter { idStaff -> onMoviemanClick(idStaff) }

    private val moviemenAdapter = MoviemenAdapter { idStaff -> onMoviemanClick(idStaff) }

    private val galleryAdapter = GalleryAdapter()

    private val relatedMoviesAdapter = RelatedMoviesAdapter { idMovie -> onMovieClick(idMovie) }

    private val reduction = Reduction()

    var idMovie = 0
    var movieName = ""
    var imdbUrl = ""
    private lateinit var relatedMovies: List<MovieRVModel>

    private lateinit var animationScaleBtn: Animation;

    private lateinit var movieRVModel: MovieRVModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idMovie = arguments?.getInt(HomeFragment.ID_MOVIE) ?: 0
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
        animationScaleBtn = AnimationUtils.loadAnimation(requireContext(), R.anim.btn_anim)

        //viewModel.setIdMovie(idMovie)
        viewModel.getRelatedMovies(idMovie)
        viewLifecycleOwner.lifecycleScope.launch {// загрузка инфо о фильме
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieInfo.collect {
                    with(binding)
                    {
                        movieName = it?.nameRu ?: ""

                        countryDurationBond.text = concatCountryDurationBond(
                            it?.countries,
                            it?.filmLength,
                            it?.ratingAgeLimits
                        )
                        desc.setText(it?.description ?: "")
                        desc.isExpanded = false
                        rateName.text = concatRateNameRu(it?.ratingKinopoisk, it?.nameRu)
                        shortDesc.text = it?.shortDescription
                        yearGenre.text = concatYearGenre(it?.year, it?.genres, it?.type)
                        Glide
                            .with(mainPoster.context)
                            .load(it?.posterUrl)
                            .into(mainPoster)

                        Glide
                            .with(logo.context)
                            .load(it?.logoUrl)
                            .into(logo)

                        if (it?.type == "TV_SERIES") {
                            headerSerial.visibility = View.VISIBLE
                            viewModel.getNumberEpisodsOfFirstSeason(idMovie)
                        } else {
                            headerSerial.visibility = View.GONE
                        }
                    }
                    imdbUrl = it?.imdbId ?: ""

                    movieRVModel = MovieRVModel(
                        it?.kinopoiskId,
                        it?.posterUrl ?: "",
                        reduction.stringReduction(it?.nameRu, 17),
                        reduction.arrayReduction(it?.genres?.map { it.genre }
                            ?: emptyList<String>(), 20, 2),
                        "0",
                        false,
                        false
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {// сохранено ли кино в коллекцию любимых фильмов - соответсвующая окраска
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isMovieInFavorite.collect {
                    if (it) {
                        binding.addToFavorites.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_of_progress
                            )
                        )
                    } else {
                        binding.addToFavorites.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_of_miss_button
                            )
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {// сохранено ли кино в коллекцию фильмов планируемых к просмотру - соответсвующая окраска
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isMovieInWantToSee.collect {
                    if (it) {
                        binding.wantToSee
                            .setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.color_of_progress
                                )
                            )
                    } else {
                        binding.wantToSee.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_of_miss_button
                            )
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {// сохранено ли кино в коллекцию просмотренных фильмов  - соответсвующая окраска
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isMovieInViewed.collect {
                    if (it) {
                        binding.viewed
                            .setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.color_of_progress
                                )
                            )
                        binding.viewed.setImageResource(R.drawable.ic_viewed)
                    } else {
                        binding.viewed.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_of_miss_button
                            )
                        )
                        binding.viewed.setImageResource(R.drawable.ic_dont_viewed)
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
            Log.d("Update", "Обновляю данные для RV на OneMovieFragment")
            relatedMoviesAdapter.setData(it.take(20))
            binding.numberOfRelatedMovies.text = "${it.size} >"
            relatedMovies = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.numberseries.onEach {// для сериала коичество серий первого эпизода
            binding.numberOfSeasonSeries.text = "1 сезон, ${it} серий >"
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        binding.numberOfActors.setOnClickListener {
            getAllActorsOrMoviemans(true)//актеры
        }

        binding.numberOfMoviemen.setOnClickListener {
            getAllActorsOrMoviemans(false)// кинематографисты
        }

        binding.numberOfGallery.setOnClickListener {//перехода на галерею
            val bundle = Bundle()
            if (idMovie != null) {
                bundle.putInt(ID_MOVIE, idMovie)
                findNavController().navigate(
                    R.id.action_oneMovieFragment_to_galleryFragment,
                    bundle
                )
            }
        }

        binding.numberOfRelatedMovies.setOnClickListener {// переход на похожие фильмы
            val bundle = Bundle()
            if (movieName != null) {
                bundle.putCharSequence(MOVIE_NAME, movieName)
                val arrayListOfRelatedMovies = ArrayList<MovieRVModel>()
                relatedMovies.map { movie -> arrayListOfRelatedMovies.add(movie) }
                bundle.putParcelableArrayList(RELATED_MOVIES_LIST, arrayListOfRelatedMovies)
                findNavController().navigate(
                    R.id.action_oneMovieFragment_to_relatedMoviesFragment,
                    bundle
                )
            }
        }
        binding.allSeasonsSeries.setOnClickListener {// переход на все сезоны сереала
            val bundle = Bundle()
            if (idMovie != null) {
                bundle.putInt(ID_MOVIE, idMovie)
                bundle.putCharSequence(MOVIE_NAME, movieName)
                findNavController().navigate(
                    R.id.action_oneMovieFragment_to_serialSeasonFragment,
                    bundle
                )
            }
        }

        binding.addToFavorites.setOnClickListener { // добавить или исключить кино из коллекции любимых фильмов
            binding.addToFavorites.startAnimation(animationScaleBtn)
            viewModel.addOrDeleteMovieToFavorite(idMovie)
        }

        binding.wantToSee.setOnClickListener { // добавить или исключить кино из коллекции фильмов к просмотру
            binding.wantToSee.startAnimation(animationScaleBtn)
            viewModel.addOrDeleteMovieToWantToSee(idMovie)
        }

        binding.viewed.setOnClickListener { // добавить или исключить кино из коллекции просмотренных фильмов
            binding.viewed.startAnimation(animationScaleBtn)
            setFragmentResult(//сигнал на предыдущий фрагмент - нужно обновить rv
                PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY,
                bundleOf(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY to true)
            )
            viewModel.addOrDeleteMovieToViewed(idMovie)
        }

        binding.share.setOnClickListener {// поделиться
            binding.share.startAnimation(animationScaleBtn)
            if (!imdbUrl.isNullOrEmpty()) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$IMDB_PATH$imdbUrl/")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            } else {
                Toast.makeText(requireContext(), "Фильма нет на www.imdb.com", Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.someMore.setOnClickListener {// добавление фильма в коллекции
            val bundle = Bundle()
            bundle.putParcelable(MOVIE, movieRVModel)
            findNavController().navigate(
                R.id.action_oneMovieFragment_to_dialogMovieToCollectionFragment,
                bundle
            )
        }
    }

    private fun getAllActorsOrMoviemans(typrOfMoviemans: Boolean) {//фрагмент со всеми фильмами кинематографиста
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(ID_MOVIE, idMovie)
            bundle.putCharSequence(MOVIE_NAME, movieName)
            bundle.putBoolean(ACTORS_OR_MOVIEMANS, typrOfMoviemans)
            findNavController().navigate(
                R.id.action_oneMovieFragment_to_allMovieMansFragment,
                bundle
            )
        }
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
        genres: List<Genre>?,
        type: String?
    ): String {// конкатинация года выпуска и жанров
        val someGenres = genres?.joinToString(
            separator = ",",
            limit = 3,
            truncated = "..."
        )
        val isSerial = if (type == "TV_SERIES") {
            "1 сезон"
        } else {
            ""
        }
        return "$year, $isSerial, $someGenres"
    }

    private fun onMoviemanClick(idStaff: Int?) {
        val bundle = Bundle()
        if (idStaff != null) {
            bundle.putInt(ID_STAFF, idStaff)
            findNavController().navigate(
                R.id.action_oneMovieFragment_to_moviemanFragment,
                bundle
            )
        }
    }

    private fun onMovieClick(idMovie: Int?) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_oneMovieFragment_self,
                bundle
            )
        }
    }

    override fun onResume() {
        super.onResume()
        //viewModel.setIdMovie(idMovie)
//        var needUpdate = false
//
//        setFragmentResultListener(HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY, bundle ->
//            if (bundle.getBoolean(HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
//                if (bundle.getBoolean(HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
//                    needUpdate = true
//                    Log.d("Update", "Обновляю RV на OneMovieFragment из RelatedMovieFragments")
//                    viewModel.getRelatedMovies(idMovie)
//                }
//            }
//        }
//        if (!needUpdate) {
//            setFragmentResultListener(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { REQUEST_KEY, bundle ->
//                if (bundle.getBoolean(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
//                    if (bundle.getBoolean(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
//                        Log.d("Update!@#$", "Обновляю RV на OneMovieFragment из OneMovieFragment")
//                        viewModel.getRelatedMovies(idMovie)
//                    }
//                }
//            }
//        }

    }

    companion object {

        const val ID_STAFF = "ID_STAFF"
        const val MOVIE_NAME = "MOVIE_NAME"
        const val ACTORS_OR_MOVIEMANS =
            "ACTORS_OR_MOVIEMANS"  // true -актеры, false - кинематографисты
        const val RELATED_MOVIES_LIST = "RELATED_MOVIES_LIST"
        const val IMDB_PATH = "https://www.imdb.com/title/"
        const val MOVIE = "MOVIE_RV_MODEL"

        //   const val RV_ITEM_HAS_BEEN_CHANGED_IN_ONEMOVIE_REQUEST_KEY="RV_ITEM_HAS_BEEN_CHANGED_IN_ONEMOVIE_REQUEST_KEY"
        //   const val RV_ITEM_HAS_BEEN_CHANGED_IN_ONEMOVIE_BUNDLE_KEY="RV_ITEM_HAS_BEEN_CHANGED_IN_ONEMOVIE_BUNDLE_KEY"

    }
}