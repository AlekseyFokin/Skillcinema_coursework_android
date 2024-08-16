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
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentOneMovieBinding
import org.sniffsnirr.skillcinema.ui.home.adapter.MainAdapter


class OneMovieFragment : Fragment() {
    private var _binding: FragmentOneMovieBinding? = null
    val binding get() = _binding!!

    private val viewModel: OneMovieViewModel by viewModels()
    private var idMovie: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idMovie = arguments?.getInt(OneMovieFragment.RV_1_NAME) ?: 0
        viewModel.setIdMovie(idMovie)
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
        viewLifecycleOwner.lifecycleScope.launch {// загрузка всего контента для HomeFragment
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieInfo.collect {
                    with(binding)
                    {
                        countryDurationBond.text = it?.countries?.joinToString(
                            separator = ",",
                            limit = 3,
                            truncated = "..."
                        )+", ${minutеsToHour(it?.filmLength)}, ${ageLimit(it?.ratingAgeLimits)} "
                        desc.text = it?.description
                        rateName.text="${it?.ratingKinopoisk.toString()} ${it?.nameRu}"
                        shortDesc.text=it?.shortDescription
                        yearGenre.text= "${it?.year} , ${it?.genres?.joinToString (separator = ",",
                            limit = 3,
                            truncated = "..."
                        )}"
                    }
                }
            }
        }
    }

    fun minutеsToHour(minutеs:Int?):String{
        if (minutеs!=null){
       val  hours= minutеs/60
       val minutes_out= minutеs%60
        return "$hours ч $minutes_out мин"}
        else { return ""}
    }

    fun ageLimit(string:String?):String{
        return if (!string.isNullOrEmpty()){
            "${string.filter { it.isDigit() }}+"
        } else ""
    }

    companion object {
        const val RV_1_NAME = "В фильме снимались"
        const val RV_2_NAME = "Над фильмом работали"
        const val RV_3_NAME = "Галерея"
        const val RV_4_NAME = "Похожие фильмы"
    }
}