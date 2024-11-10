package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel

@AndroidEntryPoint
class AllOptionsFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })

    var _binding: FragmentAllOptionsBinding? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
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
                    binding.currentRateFilter.text="${it}-${viewModel.endRate.value}"
                }
            }
        }

        //обработка установки рейтинга end
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.endRate.collect {
                    binding.currentRateFilter.text="${viewModel.startRate.value}-${it}"
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
        binding.viewed.setOnClickListener { viewModel.setViewed(!viewModel.viewed.value) }

//обработка установки viewed
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewed.collect {
                    binding.viewed.isChecked=it
                }
            }
        }

        //ОБРАБОтка установки периода

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startPeriod.collect {
                    binding.currentYearFilter.text="с ${it} по ${viewModel.endPeriod.value}"
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.endPeriod.collect {
                    binding.currentYearFilter.text="с ${viewModel.startPeriod.value} по ${it}"
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
        binding.clearYearBtn.setOnClickListener { viewModel.setStartPeriod(0)
            viewModel.setEndPeriod(0)}


    }

    fun resetAllTypes() { // скидываю в выключено
       // binding.moviesAndSerialsFilter.isChecked = false
        binding.moviesAndSerialsFilter.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )

       // binding.moviesFilter.isChecked = false
        binding.moviesFilter.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )

       // binding.serialsFilter.isChecked = false
        binding.serialsFilter.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )
    }

    fun resetAllSort() {// скидываю в выключено
        //binding.dateSort.isChecked = false
        binding.dateSort.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )

       // binding.rateSort.isChecked = false
        binding.rateSort.setTextColor(
            resources.getColor(
                R.color.color_of_main_label_in_onboarding,
                null
            )
        )

       // binding.popularSort.isChecked = false
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