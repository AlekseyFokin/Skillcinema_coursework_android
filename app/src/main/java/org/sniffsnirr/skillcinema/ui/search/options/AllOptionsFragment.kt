package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentAllOptionsBinding
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_BUNDLE_KEY
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_REQUEST_KEY
import org.sniffsnirr.skillcinema.ui.search.QueryParams
import org.sniffsnirr.skillcinema.ui.search.SearchFragment
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.ALL_TYPE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_COUNTRY
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_END_PERIOD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_END_RATE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_GENRE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_KEYWORD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_ONLY_UNVIEWED
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_START_PERIOD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_START_RATE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.SORT_DEFAULT

@AndroidEntryPoint
class AllOptionsFragment : Fragment() {

    private val viewModel: AllOptionsViewModel by viewModels({ requireParentFragment() })

    var _binding: FragmentAllOptionsBinding? = null
    val binding get() = _binding!!
lateinit var  queryParams:QueryParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
        queryParams = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable<QueryParams>(SearchFragment.QUERYPARAMS_KEY)?: QueryParams(
                DEFAULT_COUNTRY, DEFAULT_GENRE, SORT_DEFAULT, ALL_TYPE,
                DEFAULT_START_RATE, DEFAULT_END_RATE, DEFAULT_START_PERIOD, DEFAULT_END_PERIOD,
                DEFAULT_ONLY_UNVIEWED, DEFAULT_KEYWORD
            )
        } else {
            arguments?.getParcelable(
                SearchFragment.QUERYPARAMS_KEY,
                QueryParams::class.java
            )?: QueryParams(
                DEFAULT_COUNTRY, DEFAULT_GENRE, SORT_DEFAULT, ALL_TYPE,
                DEFAULT_START_RATE, DEFAULT_END_RATE, DEFAULT_START_PERIOD, DEFAULT_END_PERIOD,
                DEFAULT_ONLY_UNVIEWED, DEFAULT_KEYWORD
            )
        }

        viewModel.setQueryParams(queryParams)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle(NAME_FRAGMENT)
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideActionBar()
        setFragmentResult(//сигнал на SearchFragment - нужно обновить rv
            SearchFragment.FRAGMENT_REQUEST_KEY,
            bundleOf(SearchFragment.FRAGMENT_BANDLE_KEY to viewModel.queryParams.value)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gotoCountryOption.setOnClickListener {
            findNavController().navigate(
                R.id.action_allOptionsFragment_to_countryOptionFragment
            )
        }

        binding.gotoGenreOption.setOnClickListener {
            findNavController().navigate(
                R.id.action_allOptionsFragment_to_genreOptionFragment2
            )
        }

        binding.gotoYearOption.setOnClickListener {
            findNavController().navigate(
                R.id.action_allOptionsFragment_to_periodOptionFragment
            )
        }

        binding.rateRangeSlider.values.add(0,viewModel.startRate.value)
        binding.rateRangeSlider.values.add(1,viewModel.endRate.value)
        // установка типа
        binding.moviesAndSerialsFilter.setOnClickListener { viewModel.setType(SearchViewModel.ALL_TYPE) }
        binding.moviesFilter.setOnClickListener { viewModel.setType(SearchViewModel.MOVIE_ONLY_TYPE) }
        binding.serialsFilter.setOnClickListener { viewModel.setType(SearchViewModel.SERIAL_ONLY_TYPE) }

        viewLifecycleOwner.lifecycleScope.launch {// отработка выбранного типа
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.type.collect {
                    resetAllTypes()
                    when (it) {
                        SearchViewModel.ALL_TYPE -> {
                            binding.moviesAndSerialsFilter.isChecked = true
                            binding.moviesAndSerialsFilter.setTextColor(
                                resources.getColor(
                                    R.color.white,
                                    null
                                )
                            )
                        }
                        SearchViewModel.MOVIE_ONLY_TYPE -> {
                            binding.moviesFilter.isChecked = true
                            binding.moviesFilter.setTextColor(
                                resources.getColor(
                                    R.color.white,
                                    null
                                )
                            )
                        }
                        SearchViewModel.SERIAL_ONLY_TYPE -> {
                            binding.serialsFilter.isChecked = true
                            binding.serialsFilter.setTextColor(
                                resources.getColor(
                                    R.color.white,
                                    null
                                )
                            )
                        }
                    }
                }
            }
        }


        binding.rateRangeSlider.addOnChangeListener{slider, value, fromUser ->
            val values =binding.rateRangeSlider.values
            viewModel.setStartRate(Math.round(values[0] *10.0f)/10.0f)
            viewModel.setEndRate(Math.round(values[1] *10.0f)/10.0f)}

        //обработка установки рейтинга старт
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startRate.collect {
                    binding.currentRateFilter.text="${it?:0}-${viewModel.endRate.value?:10}"
                }
            }
        }

        //обработка установки рейтинга end
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.endRate.collect {
                    binding.currentRateFilter.text="${viewModel.startRate.value?:0}-${it?:10}"
                }
            }
        }

        // установка сортировки
        binding.popularSort.setOnClickListener { viewModel.setSort(SearchViewModel.SORT_POP) }
        binding.rateSort.setOnClickListener { viewModel.setSort(SearchViewModel.SORT_RATE) }
        binding.dateSort.setOnClickListener { viewModel.setSort(SearchViewModel.SORT_DATE) }

        viewLifecycleOwner.lifecycleScope.launch {// отработка выбранной сортировки
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sort.collect {
                    resetAllSort()
                    when (it) {
                        SearchViewModel.SORT_DEFAULT -> {}//сортировака отключена
                        SearchViewModel.SORT_POP -> {
                            binding.popularSort.isChecked = true
                            binding.popularSort.setTextColor(
                                resources.getColor(
                                    R.color.white,
                                    null
                                )
                            )
                        }
                        SearchViewModel.SORT_DATE -> {
                            binding.dateSort.isChecked = true
                            binding.dateSort.setTextColor(
                                resources.getColor(
                                    R.color.white,
                                    null
                                )
                            )
                        }
                        SearchViewModel.SORT_RATE -> {
                            binding.rateSort.isChecked = true
                            binding.rateSort.setTextColor(
                                resources.getColor(
                                    R.color.white,
                                    null
                                )
                            )
                        }
                    }
                }
            }
        }

            // обработка нажатия viewed
        binding.viewed.setOnClickListener { viewModel.setOnlyUnviewed(!viewModel.onlyUnviewed.value) }

//обработка установки viewed
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onlyUnviewed.collect {
                    binding.viewed.isChecked=it
                }
            }
        }

        //ОБРАБОтка установки периода

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startPeriod.collect {
                    var outString=""
                    if(it!=null){outString="с ${it}"}
                    if(viewModel.endPeriod.value!=null){outString=outString+" по ${viewModel.endPeriod.value}"}
                    binding.currentYearFilter.text=outString
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.endPeriod.collect {
                    var outString=""
                    if(viewModel.startPeriod.value!=null){outString="c ${viewModel.startPeriod.value}"}
                    if(it!=null){outString=outString+" по ${it}"}
                    binding.currentYearFilter.text=outString
                }
            }
        }
//обработка смены страны
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.country.collect {
                    binding.currentCountryFilter.text=it?.country?:""
                }
            }
        }
        //обработка смены жанра
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genre.collect {
                    binding.currentGenreFilter.text=it?.genre?:""                }
            }
        }
//сбросы
        binding.clearCountryBtn.setOnClickListener { viewModel.setCountry(null) }
        binding.clearGenreBtn.setOnClickListener { viewModel.setGenre(null) }
        binding.clearYearBtn.setOnClickListener { viewModel.setStartPeriod(SearchViewModel.DEFAULT_START_PERIOD)
            viewModel.setEndPeriod(SearchViewModel.DEFAULT_END_PERIOD)}


    }

    fun resetAllTypes() { // скидываю в выключено
        binding.moviesAndSerialsFilter.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )
        binding.moviesFilter.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )
        binding.serialsFilter.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )
    }

    fun resetAllSort() {// скидываю сортировку
       binding.dateSort.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )
        binding.rateSort.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )
        binding.popularSort.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )
    }



    companion object {
        const val NAME_FRAGMENT = "Настройки поиска"
    }

}