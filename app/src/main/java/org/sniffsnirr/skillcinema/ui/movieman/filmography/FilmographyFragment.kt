package org.sniffsnirr.skillcinema.ui.movieman.filmography

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentFilmographyBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment

// Фрагмент отображения фильмографии для актера и кинематографиста
@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private val viewModel: FilmographyViewModel by viewModels()

    private var _binding: FragmentFilmographyBinding? = null
    val binding get() = _binding!!
    private var staffId = 0
    private var movieManSex = "MALE"
    private val  filmographyAdapter=FilmographyAdapter{idMovie, position-> onMovieClick(idMovie, position)}
    private var possiblyEditablePosition=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        staffId = arguments?.getInt(MoviemanFragment.MOVIEMAN_ID) ?: 0
        viewModel.getMoviesByProfessionKey(staffId)
    }

    override fun onResume() {// если при восстановлении фрагмента получен сигнал об изменении данных - обновить состояние и передать выше
        super.onResume()
        (activity as MainActivity).setActionBarTitle(FRAGMENT_NAME)
        setFragmentResultListener(RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY, bundle ->
            if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
                if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
                    filmographyAdapter.updateMovieRVModel(possiblyEditablePosition)
                    setFragmentResult(
                       HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY,
                        bundleOf(HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY to true)
                    )//передаю сигнал об изменении выше
                }
            }
        }
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
        (activity as MainActivity).showActionBar()

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
                val string = SpannableString(label)// Одна строка - два размера шрифта
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
          if (it) {binding.frameProgress.visibility=View.VISIBLE}
            else{binding.frameProgress.visibility=View.INVISIBLE}
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {// ожидание ошибки
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect { _ ->
                    BottomSheetErrorFragment().show(parentFragmentManager, "errordialog")
                }
            }
        }
    }

    private fun onMovieClick(idMovie: Int?,position:Int) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(HomeFragment.ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_filmographyFragment_to_oneMovieFragment,
                bundle
            )
        }
        possiblyEditablePosition=position
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

        const val FRAGMENT_NAME="Фильмография"
    }
}
