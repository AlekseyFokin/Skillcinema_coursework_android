package org.sniffsnirr.skillcinema.ui.movieman.filmography

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
import org.sniffsnirr.skillcinema.entities.movieman.Film
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.GetMoviemanFilmographyUsecase
import javax.inject.Inject

@HiltViewModel
class FilmographyViewModel @Inject constructor(private val getMoviemanFilmographyUsecase: GetMoviemanFilmographyUsecase) : ViewModel() {

    private val _moviemanName = MutableStateFlow(Pair("",""))
    var moviemanName = _moviemanName.asStateFlow()

    private val _moviesByProfessionKey = MutableStateFlow<Map<String,List<Film>>>(emptyMap())
    var moviesByProfessionKey = _moviesByProfessionKey.asStateFlow()

    private val _moviesByListID = MutableStateFlow<List<MovieRVModel>>(emptyList())
    var moviesByListID = _moviesByListID.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    fun getMoviesByProfessionKey(idStaff:Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value=true
                getMoviemanFilmographyUsecase.getMovieByMovieman(idStaff)
            }.fold(
                onSuccess = {
                    _moviemanName.value = it.first
                    _moviesByProfessionKey.value = it.second
                                    },
                onFailure = { Log.d("Error", "Загрузка фильмографии - фильмы по кинематографисту: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
            _isLoading.value=false
        }
    }

    fun getMoviesByListId(idMoviesList:List<Int>){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value=true
                getMoviemanFilmographyUsecase.getMoviesRVModelByMoviemanProfessionKey(idMoviesList)
            }.fold(
                onSuccess = {
                        _moviesByListID.value = it
                },
                onFailure = { Log.d("Error", "Загрузка фильмов кинематографиста по professionkey: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
            _isLoading.value=false
        }
    }


}