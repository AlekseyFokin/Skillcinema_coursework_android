package org.sniffsnirr.skillcinema.ui.movieman

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.movieman.MoviemanInfo
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.GetMoviemanBestRatingMovie
import javax.inject.Inject

@HiltViewModel
class MoviemanViewModel @Inject constructor(val getMoviemanBestRatingMovie: GetMoviemanBestRatingMovie) :
    ViewModel() {
    private val _moviemanInfo = MutableStateFlow<MoviemanInfo?>(null)
    var moviemanInfo = _moviemanInfo.asStateFlow()

    private val _bestMovies = MutableStateFlow<List<MovieRVModel>>(emptyList())
    var bestMovies = _bestMovies.asStateFlow()

    private var _numberMovies = MutableStateFlow(0)
    var numberMovies = _numberMovies.asStateFlow()

    fun getBestMovies(idStaff: Int) {// получение лучших фильмов кинематографиста
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getMoviemanBestRatingMovie.getBestMoviesRVModelByMovieman(idStaff)
            }.fold(
                onSuccess = {
                    _moviemanInfo.value = it.first
                    _bestMovies.value = it.second
                    _numberMovies.value = it.third
                },
                onFailure = { Log.d("bestMoviesMovies", it.message ?: "") }
            )
        }
    }

}