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
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentFilmographyBinding
import org.sniffsnirr.skillcinema.ui.movieman.MoviemanFragment


@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private val viewModel: FilmographyViewModel by viewModels()

    private var _binding: FragmentFilmographyBinding? = null
    val binding get() = _binding!!
    private var staffId = 0
    private var movieManSex = "MALE"

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
                Log.d("key", "${item.key}")

                val chip = Chip(binding.filmographyChipgroup.context)
                // necessary to get single selection working
                // chip.isClickable = true
                chip.isCheckedIconVisible = false
                chip.isCheckable = true
                chip.setChipBackgroundColorResource(R.color.chip_color_selector)
                var label = StringBuilder(SET_OF_PROFESSION_KEY.get(item.key))

                if (label.isNullOrEmpty()) {
                    label = StringBuilder("Прочее")
                }
                if ((label.toString() == "Актер") && (movieManSex == "FEMALE")) {
                    label = StringBuilder("Актриса")
                }
                label = label.append("   ${item.value.size}")
                val string = SpannableString(label.toString())
                string.setSpan(
                    RelativeSizeSpan(0.7f),
                    string.lastIndexOf(" "),
                    string.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                chip.text = string
                chip.setChipStrokeWidth(2.0f)
                chip.setChipStrokeColorResource(R.color.black)
                chip.setCheckableResource()
                binding.filmographyChipgroup.addView(chip)

            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
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
            Pair("HRONO_TITR_FEMALE", "Актриса дубляжа")
        )
    }
}
