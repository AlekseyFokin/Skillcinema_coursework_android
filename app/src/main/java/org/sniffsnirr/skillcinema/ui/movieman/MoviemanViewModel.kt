package org.sniffsnirr.skillcinema.ui.movieman

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.movieman.MoviemanInfo
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.GetMoviemanBestRatingMovieUsecase
import javax.inject.Inject

@HiltViewModel
class MoviemanViewModel @Inject constructor(private val getMoviemanBestRatingMovieUsecase: GetMoviemanBestRatingMovieUsecase) :
    ViewModel() {
    private val _moviemanInfo = MutableStateFlow<MoviemanInfo?>(null)
    var moviemanInfo = _moviemanInfo.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    private val _bestMovies = MutableStateFlow<List<MovieRVModel>>(emptyList())
    var bestMovies = _bestMovies.asStateFlow()

    private var _numberMovies = MutableStateFlow(0)
    var numberMovies = _numberMovies.asStateFlow()

    fun getBestMovies(idStaff: Int) {// получение лучших фильмов кинематографиста
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getMoviemanBestRatingMovieUsecase.getBestMoviesRVModelByMovieman(idStaff)
            }.fold(
                onSuccess = {
                    _moviemanInfo.value = it.first
                    _bestMovies.value = it.second
                    _numberMovies.value = it.third
                },
                onFailure = {Log.d("Error", "Загрузка лучшие фильмы кинематографиста: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
        }
    }
}